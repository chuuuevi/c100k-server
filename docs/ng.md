# nginx

```bash
docker run --rm \
  -it \
  -d \
  --network=host \
  nginx
```


# server is r7iz.2xlarge, wrk is c5.xlarge

## nginx

```text
docker run --rm -it qyvlik/wrk -t 4 -c 128 -d 30s http://172.31.15.175
Running 30s test @ http://172.31.15.175
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   755.61us  304.97us  13.31ms   74.87%
    Req/Sec    29.32k     2.17k   36.26k    69.25%
  3500958 requests in 30.02s, 2.78GB read
Requests/sec: 116627.55
Transfer/sec:     94.87MB
```
