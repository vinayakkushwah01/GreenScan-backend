package com.greenscan.dto.request;

import lombok.Data;

@Data
public class EndUserProfileRequest {
   Long mainUserId;
   String preferedPickupTime;
   Long preferdVendorId;

  public EndUserProfileRequest(Long id){
   this.mainUserId = id ;

  }  
}
