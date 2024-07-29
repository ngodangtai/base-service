package com.company.module.base.lock;

import com.company.module.common.ELockType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LockerFactory {

    private Map<ELockType, Locker> lookup;

    private final List<Locker> lockers;

    @PostConstruct
    private void init() {
        lookup = new EnumMap<>(ELockType.class);
        lockers.forEach(l -> lookup.put(l.getType(), l));
    }

    public Locker getLocker(ELockType type) {
        return lookup.get(type);
    }
}
