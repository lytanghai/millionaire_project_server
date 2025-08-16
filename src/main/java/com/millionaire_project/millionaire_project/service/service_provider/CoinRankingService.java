package com.millionaire_project.millionaire_project.service.service_provider;

import com.millionaire_project.millionaire_project.annotation.ServiceProvider;
import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.constant.TopicOperation;
import com.millionaire_project.millionaire_project.controller.service_provider.ServiceAPIProvider;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.service.service_provider.coin_ranking.Coin;
import com.millionaire_project.millionaire_project.util.CommonUtil;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ServiceProvider(partnerCode = "0005", partnerName = "coin_ranking")
public class CoinRankingService implements ServiceAPIProvider {

    @Autowired private Coin coin;

    @Override
    public String getPartnerName() {
        return "coin_ranking";
    }

    @Override
    public ResponseBuilder<ApiResponder> trigger(ApiRequester apiRequester) throws Exception {
        if(apiRequester != null) {
            JSONObject payload = new JSONObject(apiRequester.getPayload());
            String topicName = payload.optString("topic_operation");
            TopicOperation topic = TopicOperation.fromTopicName(topicName);
            String coinId = CommonUtil.getCoinUUID(payload.optString("coin"));
            if (topic != null) {
                switch (topicName) {
                    case Static.CR_GET_COIN_DETAIL : return coin.getCoinDetail(coinId, topic.getEndpoint(), apiRequester.getProviderName());
                    default: throw new ServiceException(ApplicationCode.S02.getCode(),ApplicationCode.S02.getMessage());
                }
            } else {
                throw new ServiceException(ApplicationCode.S01.getCode(),ApplicationCode.S01.getMessage());
            }
        }
        return null;
    }

}
