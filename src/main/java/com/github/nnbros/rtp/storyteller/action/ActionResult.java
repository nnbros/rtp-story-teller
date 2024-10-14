package com.github.nnbros.rtp.storyteller.action;

public record ActionResult<T>(T value, boolean isSuccessful) {
}
