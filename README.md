# Indexer

Reactive library for Java

## Usage

First, you need an object with ObservableField or ObservableSet

```java
public class Nation {

    private final ObservableField<Nation, UUID> leader;
    private final ObservableSet<Nation, Factory> factories;

    public Nation(UUID leader) {
        this.leader = new ObservableField(this, leader);
        this.factories = new ObservableSet(this);
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
        this.supervisor = new ObservableField(this, supervisor);
        this.members = new ObservableSet(this);
    }
}
```

Both ObservableField and ObservableSet have **owner** field. This field is used to create/update index. Without this field, Index does not work well.

Then, you have to create a list of all entities from which you will look up nation by its leader or factories.

You can also create a list of factories of all nations, and create index from that list.

SimpleObservableSet is a special type of ObservableSet. SimpleObservableSet does not have its owner.

```java
public class Main {
    public static void main() {
        SimpleObservableSet<Nation> nations = new SimpleObservableSet();

        // look up nation by its leader
        Map<UUID, Nation> leaderIndex = nations.createIndex(Nation::getLeader);

        // look up nation by its factories
        Map<Factory, Nation> factoryIndex = nations.createFlatIndex(Nation::getFactories);

        // list all factories
        SimpleObservableSet<Factory> factories = nations.createFlatMap(Nation::getFactories);

        // look up factory by its supervisor
        Map<UUID, Factory> supervisorIndex = factories.createIndex(Factory::getSupervisor);

        // look up factory by its members (A member can work for multiple factories)
        Map<UUID, Set<Factory>> memberIndex = factories.createMultiIndex(Factory::getMembers);
    }
}
```
