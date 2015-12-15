package com.minicluster.example.reducible;

import com.minicluster.cluster.service.Reducible;

import java.io.Serializable;

/**
 * Reduce = Multiply
 */
public class Multipliable implements Serializable, Reducible {

    private static final long serialVersionUID = 1L;
    private Double value;

    public Multipliable(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return this.value;
    }

    @Override
    public void reduce(Reducible data) {
        this.value *= ((Multipliable) data).getValue();
    }
}
