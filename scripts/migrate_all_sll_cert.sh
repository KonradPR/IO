#!/bin/bash

/home/ubuntu/deployment/scripts/convert_ssl.sh /home/ubuntu/secrets/origin_a_com.p12 /home/ubuntu/secrets/nginx_certs/origin_a_com.crt.pem /home/ubuntu/secrets/nginx_certs/origin_a_com.key.pem
/home/ubuntu/deployment/scripts/convert_ssl.sh /home/ubuntu/secrets/origin_b_com.p12 /home/ubuntu/secrets/nginx_certs/origin_b_com.crt.pem /home/ubuntu/secrets/nginx_certs/origin_b_com.key.pem