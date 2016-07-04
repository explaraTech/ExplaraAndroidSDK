package com.explara.explara_ticketing_sdk.attendee;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk.attendee.io.AttendeeConnectionManager;
import com.explara.explara_ticketing_sdk.common.BaseManager;
import com.explara.explara_ticketing_sdk.utils.Callback;
import com.explara.explara_ticketing_sdk.utils.ExplaraError;
import com.explara_core.utils.ConstantKeys;

import java.util.Collection;
import java.util.List;

/**
 * Created by anudeep on 08/01/16.
 */
public class AttendeeManager extends BaseManager {

    private static AttendeeManager sAttendeeManager;
    public AttendeeDetailsResponseDto attendeeDetailsResponseDto;

    /*public interface AttendeeDownloadListener {
        void onAttendeeFormDownloadSuccess(AttendeeDetailsResponseDto attendeeDetailsResponseDto);

        void onAttendeeFormDownloadFailed(VolleyError volleyError);
    }*/

    private AttendeeManager() {

    }

    public static AttendeeManager getInstance() {
        if (sAttendeeManager == null) {
            sAttendeeManager = new AttendeeManager();
        }
        return sAttendeeManager;
    }


    public void downloadAttendeeFormData(Context context, Callback<AttendeeDetailsResponseDto> attendeeDownloadListener, String tag) {
        AttendeeConnectionManager attendeeConnectionManager = new AttendeeConnectionManager();
        attendeeConnectionManager.downloadAttendeeFormData(context, getDownloadSuccessListener(attendeeDownloadListener), getDownloadFailListener(attendeeDownloadListener), tag);
    }

    private Response.Listener<AttendeeDetailsResponseDto> getDownloadSuccessListener(final Callback<AttendeeDetailsResponseDto> attendeeDownloadListener) {
        return new Response.Listener<AttendeeDetailsResponseDto>() {
            @Override
            public void onResponse(AttendeeDetailsResponseDto response) {
                attendeeDetailsResponseDto = prepareAttendeeResponseForFileUpload(response);
                attendeeDownloadListener.success(response);
            }
        };

    }

    private Response.ErrorListener getDownloadFailListener(final Callback<AttendeeDetailsResponseDto> attendeeDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                attendeeDownloadListener.error(new ExplaraError(error));
            }
        };
    }

    private AttendeeDetailsResponseDto prepareAttendeeResponseForFileUpload(AttendeeDetailsResponseDto attendeeDetailsResponseDto) {
        if (attendeeDetailsResponseDto != null && attendeeDetailsResponseDto.attendeeform != null) {
            int requestCode = 1;
            Collection<List<AttendeeDetailsResponseDto.AttendeeDto>> values = attendeeDetailsResponseDto.attendeeform.values();
            for (List<AttendeeDetailsResponseDto.AttendeeDto> attendeeListDto : values) {
                for (AttendeeDetailsResponseDto.AttendeeDto attendeeDto : attendeeListDto) {
                    if (ConstantKeys.AttendeeFormTypes.FILE.equals(attendeeDto.type)) {
                        attendeeDto.requestCodeFileUpload = requestCode++;
                    }
                }
            }
            return attendeeDetailsResponseDto;
        }
        return null;
    }

    public AttendeeDetailsResponseDto.AttendeeDto getAttendeeDtoByRequestCode(int requestCode) {
        Collection<List<AttendeeDetailsResponseDto.AttendeeDto>> values = attendeeDetailsResponseDto.attendeeform.values();
        for (List<AttendeeDetailsResponseDto.AttendeeDto> attendeeListDto : values) {
            for (AttendeeDetailsResponseDto.AttendeeDto attendeeDto : attendeeListDto) {
                if (attendeeDto.requestCodeFileUpload == requestCode) {
                    return attendeeDto;
                }
            }
        }
        return null;
    }

    public int getAttendeeSequenceByAttendeeDto(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        Collection<List<AttendeeDetailsResponseDto.AttendeeDto>> values = attendeeDetailsResponseDto.attendeeform.values();
        int attendeeSequence = 0;
        for (List<AttendeeDetailsResponseDto.AttendeeDto> attendeeListDto : values) {
            for (AttendeeDetailsResponseDto.AttendeeDto attendeeDtoCurrent : attendeeListDto) {
                if (attendeeDtoCurrent.requestCodeFileUpload == attendeeDto.requestCodeFileUpload) {
                    return attendeeSequence;
                }
            }
            attendeeSequence = attendeeSequence + 1;
        }
        return 0;
    }

    @Override
    public void cleanUp() {
        sAttendeeManager = null;
        attendeeDetailsResponseDto = null;
    }
}
