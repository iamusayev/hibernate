Entity Life cycle

Transient

Persistent

Detached

Removed

Detached -> close(), evict(entity), clear() ->  Persistent    saveOrUpdate(entity),  update(entity), merge(entity)

![](../../../Desktop/1.png)