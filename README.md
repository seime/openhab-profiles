# Seime Profiles

A set of additional openHAB profiles.

## Redeliver Command Profile

This profile redelivers commands sent from an item to a handler until the handler updates the channel to the expected state. This is
useful i.e. when a device or service is temporarily unavailable, and the binding does not have any retry mechanism built in.

Does *not* work for channels that report a different value back after receiving a command, ie a `RollerShutter` that is sent a `DOWN` command but reports a percent value back.

Noteworthy:

* Any time consumed by the handler itself is in addition the specified delay.
* Any subsequent commands sent to the item will cancel existing redeliveries and schedule new ones.
* State updates sent to the item from openHAB (ie `openhab:update xxx ON`) will also cancel any redeliveries.
* Changes to the profile configuration parameters are _not_ reflected until openHAB or the bundle is restarted.
* There is no persistence involved, so any redelivery will not survive a restart.

### Configuration

| Configuration Parameter | Type    | Description                                                                   |
|-------------------------|---------|-------------------------------------------------------------------------------|
| `numRedeliveries`       | integer | The number of redeliveries (in *addition* to the original attempt). Default 1 |
| `redeliverDelayMillis`  | integer | The number of milliseconds between each redelivery attempt. Default 1000      |

### Full Example

Redeliver `valve` (`ON`/`OFF`) command until the channel is updated with the same state by the handler. Will redeliver command with an interval of `1000ms` for up to a total of 4 times (original attempt + 3 redeliveries).

```
Switch valve {channel="xxx" [profile="seime-profiles:redeliver-command", numRedeliveries=3, redeliverDelayMillis=1000] }
```

## Building

```
mvn -P standalone clean verify
```
