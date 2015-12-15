package com.minicluster.example.reducible;

import com.minicluster.cluster.service.Reducible;

import java.io.Serializable;

/**
 * Reduce = Sum
 */
public class Summable implements Serializable, Reducible {

    private static final long serialVersionUID = 1L;
    private Integer value;

    public Summable(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    @Override
    public void reduce(Reducible data) {
        this.value += ((Summable) data).getValue();
    }
}
