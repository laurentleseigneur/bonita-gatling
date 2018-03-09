# Bonita-gatling
use Gatling for Bonita REST API perf benchmark

## setup

install scala + sbt (osx)

```
brew update
brew install scala
brew install sbt
```

generate project 

```
mvn archetype:generate
# choose gatling
```

for recorder, configure a proxy

### troubleshooting

[fix pom issue](https://github.com/gatling/gatling/issues/3361)

## Realtime monitoring with influxDB + Grafana

https://gatling.io/docs/current/realtime_monitoring/

```
# influxDB
docker run -p 8086:8086 -p 2003:2003 -e INFLUXDB_GRAPHITE_ENABLED=true influxdb
```

configure `/etc/influxdb/influxdb.conf` in the container

```
[meta]
  dir = "/var/lib/influxdb/meta"

[data]
  dir = "/var/lib/influxdb/data"
  engine = "tsm1"
  wal-dir = "/var/lib/influxdb/wal"
[[graphite]]
        enabled = true
        database = "gatlingdb"

        templates = [
                "gatling.*.*.*.* measurement.simulation.request.status.field",
                "gatling.*.users.*.* measurement.simulation.measurement.request.field"
        ]


```

and restart container


# grafana
docker run -d --name=grafana -p 3000:3000 grafana/grafana
```

[grafana dashboard](https://raw.githubusercontent.com/gatling/gatling/master/src/sphinx/realtime_monitoring/code/gatling.json)


