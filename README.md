# ddns [![ddns Docker Image CI](https://github.com/xupeixuan/ddns/actions/workflows/docker-image.yml/badge.svg)](https://github.com/xupeixuan/ddns/actions/workflows/docker-image.yml)

ddns for alicloud

## Usage

docker-compose.yaml example

```yaml
version: "3.9"

services:
  ddns:
    image: ghcr.io/xupeixuan/ddns:v0.1
    container_name: ddns
    restart: always
    network_mode: "host"
    environment:
      ALIDNS_SECRET_FILE: /run/secrets/alidns_secret
      ALIDNS_ACCESS_KEY_FILE: /run/secrets/alidns_access_key
      ALIDNS_DOMAIN: baidu.com
      ALIDNS_SUB_DOMAINS: www
      ALIDNS_TYPE: AAAA  #IPv6 or IPv4
    secrets:
      - alidns_access_key
      - alidns_secret
    healthcheck:
      test: "wget -qO- http://127.0.0.1:9000/actuator/health | grep UP || exit 1"
      start_period: 60s
      interval: 300s
      timeout: 60s
      retries: 6

secrets:
  alidns_secret:
    file: path/alidns-secret.txt
  alidns_access_key:
    file: path/alidns-access_key.txt
```
