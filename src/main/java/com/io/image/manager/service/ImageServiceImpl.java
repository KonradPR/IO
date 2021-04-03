package com.io.image.manager.service;

import com.io.image.manager.exceptions.ImageOperationException;
import com.io.image.manager.cache.ImageCache;
import com.io.image.manager.config.AppConfigurationProperties;
import com.io.image.manager.service.operations.ImageOperation;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    private final AppConfigurationProperties props;
    private final ImageCache cache;

    public ImageServiceImpl(AppConfigurationProperties props, ImageCache cache) {
        this.props = props;
        this.cache = cache;
    }

    @Override
    public Optional<BufferedImage> fetchAndCacheImage(String filename, List<ImageOperation> operations) throws IOException, ImageOperationException {
        Optional<BufferedImage> image = fetchLocalImage(filename, operations);
        if (image.isPresent()) {
            return image;
        }

        image = fetchRemoteImage(filename) ;
        if (image.isPresent()) {
            var img = image.get();
            for (var op: operations) {
                img = op.run(img);
            }
            cache.storeImage(img, filename, operations);

            return Optional.of(img);
        }
        return Optional.empty();
    }

    private Optional<BufferedImage> fetchRemoteImage(String filename) {
        try {
            // FIXME: this does not look pretty
            URL url = new URL(props.getOriginServer() + filename);
            BufferedImage image = ImageIO.read(url.openStream());
            if (image == null) {
                return Optional.empty();
            } else {
                return Optional.of(image);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<BufferedImage> fetchLocalImage(String filename, List<ImageOperation> operations) throws IOException {
            return cache.loadImage(filename, operations);
    }
}
