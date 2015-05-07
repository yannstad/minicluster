package com.yann.distributedenv.allreduce;

import java.io.IOException;
import java.io.Serializable;

import com.yann.distributedenv.comm.Reducable;

interface AllReducable {

	<T extends Reducable & Serializable> T allReduce(T data) throws ClassNotFoundException, IOException;
}
