package com.exc.street.light.resource.dto;

import lombok.Data;

@Data
public class WebsocketQuery<T> {
	private Integer type;
	private T data;
}
