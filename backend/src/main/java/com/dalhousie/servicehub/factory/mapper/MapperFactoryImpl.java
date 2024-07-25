package com.dalhousie.servicehub.factory.mapper;

import com.dalhousie.servicehub.mapper.FeedbackMapper;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperFactoryImpl implements MapperFactory {

    private final ServiceMapper serviceMapper;
    private final FeedbackMapper feedbackMapper;

    @Override
    public ServiceMapper getServiceMapper() {
        return serviceMapper;
    }

    @Override
    public FeedbackMapper getFeedbackMapper() {
        return feedbackMapper;
    }
}
