package com.skt.ems.formbuilder.client.event.dto.response;

import com.skt.ems.common.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private String id;
    private String status;
    private String secondaryColor;
    private String name;
    private String about;
    private String subdomain;
    private LocalDate startDate;
    private LocalDate endDate;
    private String timeZone;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venueName;
    private Address address;
    private String primaryColor;
    private String logoUrl;
    private String eventBannerUrl;

    private Boolean websiteCmsFlag;
    private Boolean registrationMgtFlag;
    private Boolean stakeholderPortalFlag;
    private Boolean mobileAppBuilderFlag;
    private Boolean participationMgtFlag;

    private Boolean exhibitorFlag;
    private Boolean visitorFlag;
    private Boolean buyerFlag;
    private Boolean vendorFlag;
    private Boolean delegateFlag;
    private Boolean vvipFlag;
    private Boolean sponsorFlag;
    private Boolean partnerFlag;
    private Boolean mediaFlag;
    private Boolean volunteerFlag;
    private Boolean vipFlag;
}
