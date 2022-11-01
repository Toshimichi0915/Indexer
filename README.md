# Indexer

A reactive library for Java

## Installation

### Snapshot

```groovy
repositories {
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots' }
}

dependencies {
    implementation 'net.toshimichi:indexer:1.1.0-SNAPSHOT'
}
```

### Release

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'net.toshimichi:indexer:1.0.0'
}
```

## Usage

### Basic

First, you need an object with ObservableField or ObservableSet

```java
public class Nation {

    private final ObservableField<Nation, UUID> leader;
    private final ObservableSet<Nation, Factory> factories;

    public Nation(UUID leader) {
        this.leader = new ObservableField<>(leader);
        this.factories = new ObservableSet<>();
    }

    public ObservableField<Nation, UUID> getLeader() {
        return leader;
    }

    public ObservableSet<Nation, Factory> getFactories() {
        return factories;
    }
}
```

```java
public class Factory {

    private final ObservableField<Factory, UUID> supervisor;
    private final ObservableSet<Factory, UUID> members;

    public Factory(UUID supervisor) {
        this.supervisor = new ObservableField<>(supervisor);
        this.members = new ObservableSet<>();
    }
}
```

Both ObservableField and ObservableSet have an **owner** field. This field is used to create/update indexes. Without this
field, indexes does not work well.

Then, you have to create a list of all entities from which you will look up a nation by its leader or factories.

You can also create a list of factories of all nations, and create an index from that list.

```java
public class Main {
    public static void main() {
        ObservableSet<Object, Nation> nations = new ObservableSet<>();

        // look up nation by its leader
        Map<UUID, Nation> leaderIndex = nations.createIndex(Nation::getLeader);

        // look up nation by its factories
        Map<Factory, Nation> factoryIndex = nations.createFlatIndex(Nation::getFactories);

        // list all factories
        ObservableSet<Factory> factories = nations.createFlatMap(Nation::getFactories);

        // look up factory by its supervisor (A supervisor can work for only one factory)
        Map<UUID, Factory> supervisorIndex = factories.createIndex(Factory::getSupervisor);

        // look up factory by its supervisor (A supervisor can work for multiple factories)
        map<UUID, Set<Factory>> supervisorIndex2 = factories.createMultiIndex(Factory::getSupervisor);

        // look up factory by its members (A member can work for multiple factories)
        Map<UUID, Set<Factory>> memberIndex = factories.createFlatMultiIndex(Factory::getMembers);
    }
}
```
