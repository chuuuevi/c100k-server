
# server is MacBookPro M4(MacOS 15.3.1)

## jdk21-server with virtual thread

```text
wrk -t 4 -c 128 -d 30s http://127.0.0.1:22222
Running 30s test @ http://127.0.0.1:22222
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.16ms    3.54ms 144.71ms   98.79%
    Req/Sec    35.67k     5.11k   41.03k    92.08%
  4260317 requests in 30.04s, 332.10MB read
  Socket errors: connect 0, read 138, write 0, timeout 0
Requests/sec: 141819.38
Transfer/sec:     11.06MB
```

```text
wrk -t 2 -c 190 -d 30s http://127.0.0.1:22222
Running 30s test @ http://127.0.0.1:22222
  2 threads and 190 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.93ms  389.67us   4.94ms   78.39%
    Req/Sec    86.81k     2.76k   92.89k    72.83%
  5183111 requests in 30.00s, 410.27MB read
  Socket errors: connect 0, read 160, write 0, timeout 0
Requests/sec: 172760.00
Transfer/sec:     13.67MB
```


# server is c6i.2xlarge, wrk is t3.xlarge

## vertx-server with virtual thread

```text
docker run --rm -it qyvlik/wrk -t 4 -c 128 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.31ms    3.38ms 103.42ms   97.91%
    Req/Sec    15.20k     2.50k   17.62k    90.25%
  1817753 requests in 30.06s, 122.02MB read
Requests/sec:  60474.25
Transfer/sec:      4.06MB
```

## jdk21-server with virtual thread

```text
docker run --rm -it qyvlik/wrk -t 4 -c 128 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.00ms  726.86us  18.86ms   94.45%
    Req/Sec    15.65k     1.60k   18.13k    69.42%
  1870264 requests in 30.05s, 146.26MB read
Requests/sec:  62238.05
Transfer/sec:      4.87MB
```

# server is r7iz.2xlarge, wrk is t3.xlarge

## jdk21-server with virtual thread

```text
docker run --rm -it qyvlik/wrk -t 4 -c 128 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.96ms    3.02ms 210.76ms   99.20%
    Req/Sec    16.44k     2.33k   18.55k    91.99%
  1960191 requests in 30.03s, 152.23MB read
Requests/sec:  65278.12
Transfer/sec:      5.07MB
```

## vertx-server with virtual thread

```text
 docker run --rm -it qyvlik/wrk -t 4 -c 128 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.04ms    2.93ms  97.97ms   98.16%
    Req/Sec    16.94k     2.02k   19.19k    89.83%
  2025090 requests in 30.05s, 136.06MB read
Requests/sec:  67389.46
Transfer/sec:      4.53MB
```

# server is r7iz.2xlarge, wrk is c5.xlarge

## jdk21-server with virtual thread

```text
docker run --rm -it qyvlik/wrk -t 4 -c 128 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   743.23us  311.82us  11.89ms   79.91%
    Req/Sec    30.95k     1.03k   33.33k    80.08%
  3695409 requests in 30.02s, 292.10MB read
Requests/sec: 123098.29
Transfer/sec:      9.73MB
```

```text
docker run --rm -it qyvlik/wrk -t 4 -c 256 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 256 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.39ms  576.88us  16.50ms   75.75%
    Req/Sec    23.93k     1.02k   32.84k    76.67%
  2859455 requests in 30.08s, 223.61MB read
  Socket errors: connect 0, read 308865, write 0, timeout 0
Requests/sec:  95057.99
Transfer/sec:      7.43MB
```

## vertx-server with virtual thread

```text
docker run --rm -it qyvlik/wrk -t 4 -c 128 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.35ms  117.79us  15.17ms   93.81%
    Req/Sec    23.70k   353.95    24.48k    80.83%
  2829395 requests in 30.01s, 191.58MB read
Requests/sec:  94286.83
Transfer/sec:      6.38MB
```

```text
docker run --rm -it qyvlik/wrk -t 4 -c 256 -d 30s http://172.31.15.175:22221
Running 30s test @ http://172.31.15.175:22221
  4 threads and 256 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.69ms  131.46us  14.83ms   88.43%
    Req/Sec    23.82k   317.41    25.98k    71.75%
  2843861 requests in 30.03s, 192.56MB read
Requests/sec:  94716.42
Transfer/sec:      6.41MB
```

