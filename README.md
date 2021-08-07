# Beaming
![Beaming](https://github.com/APDevTeam/Beaming/actions/workflows/maven.yml/badge.svg)

Beaming - a companion to Movecraft


## Version support
The `legacy` branch is coded for 1.10.2 to 1.16.5 and Movecraft 7.x.

The `main` branch is coded for 1.14.4 to 1.17.1 and Movecraft 8.x.

## Download
Devevlopment builds can be found on the [GitHub Actions tab](https://github.com/APDevTeam/Beaming/actions) of this repository.

Stable builds can be found on [our SpigotMC page](https://www.spigotmc.org/resources/beaming.8094/).

## Building
This plugin requires that the user setup their GitHub token in maven to authenticate with GitHub Packages, as described in [this wiki page](https://github.com/APDevTeam/Movecraft/wiki/Documentation).

Then, run the following to build Beaming through `maven`.
```
mvn clean install
```
Jars are located in `/target`.


### Commands:
/beam - Beams you to your airship, requires that you have a crew sign

### Permissions:
/beam - beaming.beam
