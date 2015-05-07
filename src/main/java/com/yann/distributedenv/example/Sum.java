package com.yann.distributedenv.example;

import java.io.Serializable;

import com.yann.distributedenv.comm.Reducable;

public class Sum implements Serializable, Reducable {

	private static final long serialVersionUID = 1L;
	private Integer _value;

	public Integer getSum() {
		return _value;
	}

	public Sum(Integer sum) {
		_value = sum;
	}

	@Override
	public void reduce(Reducable data) {
		_value += ((Sum) data).getSum();
	}
}
