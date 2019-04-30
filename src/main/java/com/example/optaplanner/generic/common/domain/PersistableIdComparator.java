package com.example.optaplanner.generic.common.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.io.Serializable;
import java.util.Comparator;

public class PersistableIdComparator implements Comparator<AbstractPersistable>, Serializable {

    @Override
    public int compare(AbstractPersistable a, AbstractPersistable b) {
        return new CompareToBuilder().append(a.getId(), b.getId()).toComparison();
    }

}
