package org.fast.spirit.info;

public class ApiInfoBuilder extends ApiInfo {
  public ApiInfoBuilder() {
    super();
  }

  public ApiInfoBuilder title(String title) {
    super.title = title;
    return this;
  }
  public ApiInfoBuilder version(String version) {
    this.version = version;
     return this;
  }
  public ApiInfoBuilder logoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
    return this;
  }
}
