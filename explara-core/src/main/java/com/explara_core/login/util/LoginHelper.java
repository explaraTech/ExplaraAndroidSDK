package com.explara_core.login.util;

import com.explara_core.common_dto.ProfileResponse;
import com.explara_core.common_dto.Profile;
import com.explara_core.login.login_dto.LoginResponseDto;

/**
 * Created by anudeep on 06/10/15.
 */
public class LoginHelper {
    private static final String TAG = LoginHelper.class.getSimpleName();

    public static ProfileResponse getUserProfile(LoginResponseDto loginResponse) {
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setStatus(loginResponse.status);
        Profile profile = new Profile();
        profile.setFirstName(loginResponse.account.firstName);
        profile.setLastName(loginResponse.account.lastName);
        profile.setEmailId(loginResponse.account.emailId);
        profile.setMobileNumber(loginResponse.account.mobileNumber);
        profile.setProfileImage(loginResponse.account.profileImage);
        profileResponse.setProfile(profile);
        return profileResponse;
    }
}
