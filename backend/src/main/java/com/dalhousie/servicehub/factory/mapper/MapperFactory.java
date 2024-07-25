package com.dalhousie.servicehub.factory.mapper;

import com.dalhousie.servicehub.mapper.FeedbackMapper;
import com.dalhousie.servicehub.mapper.ServiceMapper;

public interface MapperFactory {
    ServiceMapper getServiceMapper();
    FeedbackMapper getFeedbackMapper();
}
