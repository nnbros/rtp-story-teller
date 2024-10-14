package com.github.nnbros.rtp.storyteller.action;

public interface Action<T extends Action<T>> {

	String getActionName();

	T getAction();
}
