package com.moviebooking.api.dto;


import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationRequestDTO {

	@NotNull
	private String userName;
	@NotNull
	private String password;
}
