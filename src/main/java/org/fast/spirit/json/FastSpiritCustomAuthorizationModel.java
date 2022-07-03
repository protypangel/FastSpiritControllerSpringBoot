package org.fast.spirit.json;

import lombok.Getter;
import org.fast.spirit.annotation.custom.FastSpiritCustomAuthorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FastSpiritCustomAuthorizationModel {
  private List<String> authorizationApi = new ArrayList<>(), authorizationDoc = new ArrayList<>();
  public FastSpiritCustomAuthorizationModel(FastSpiritCustomAuthorization authorization) {
    authorizationApi.addAll(Arrays.stream(authorization.roles().api()).map(this::setAuthorization).collect(Collectors.toList()));
    authorizationApi.addAll(Arrays.stream(authorization.roles().both()).map(this::setAuthorization).collect(Collectors.toList()));
    authorizationApi.addAll(Arrays.stream(authorization.roles().value()).map(this::setAuthorization).collect(Collectors.toList()));
    authorizationApi.addAll(Arrays.asList(authorization.privileges().api()));
    authorizationApi.addAll(Arrays.asList(authorization.privileges().both()));
    authorizationApi.addAll(Arrays.asList(authorization.privileges().value()));


    authorizationDoc.addAll(Arrays.stream(authorization.roles().doc()).map(this::setAuthorization).collect(Collectors.toList()));
    authorizationDoc.addAll(Arrays.stream(authorization.roles().both()).map(this::setAuthorization).collect(Collectors.toList()));
    authorizationDoc.addAll(Arrays.stream(authorization.roles().value()).map(this::setAuthorization).collect(Collectors.toList()));
    authorizationDoc.addAll(Arrays.asList(authorization.privileges().doc()));
    authorizationDoc.addAll(Arrays.asList(authorization.privileges().both()));
    authorizationDoc.addAll(Arrays.asList(authorization.privileges().value()));
  }

  private String setAuthorization(String s) {
    return "ROLE_" + s;
  }
}
