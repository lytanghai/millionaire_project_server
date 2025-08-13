package com.millionaire_project.millionaire_project.service.service_provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.annotation.PartnerServiceRegistry;
import com.millionaire_project.millionaire_project.annotation.ServiceProvider;
import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.controller.service_provider.ServiceAPIProvider;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.repository.CredentialRepository;
import com.millionaire_project.millionaire_project.service.CredentialService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import com.millionaire_project.millionaire_project.util.RestTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@ServiceProvider(partnerCode = "0001", partnerName = "crypto_panic")
public class CryptoPanicService implements ServiceAPIProvider {

//    Mostly for Related News Only
    @Override
    public String getPartnerName() {
        return "crypto_panic";
    }

    private final Logger log = LoggerFactory.getLogger(CryptoPanicService.class);
    @Autowired private PartnerServiceRegistry registry;
    @Autowired private CredentialRepository credentialRepository;
    @Autowired private CredentialService credentialService;

    public static final List<String> AVAILABLE_FILTER = Arrays.asList("rising", "hot", "bullish", "bearish", "important", "saved" , "lol");
    public static final List<String> AVAILABLE_KIND = Arrays.asList("news", "media", "all");

    @Override
    public ResponseBuilder<ApiResponder> trigger(ApiRequester dto) throws JsonProcessingException {
        if (dto.getTopicName() == null || dto.getTopicName().isEmpty())
            throw new ServiceException(ApplicationCode.E006.getCode(), ApplicationCode.E006.getMessage());

        String providerName = dto.getProviderName();

        List<CredentialEntity> credentialObject = credentialRepository.findByProviderName(providerName);
        Optional<CredentialEntity> getRemaining = credentialObject.stream()
                .filter(c -> c.getProviderName().equalsIgnoreCase(providerName))
                .max(Comparator.comparingInt(CredentialEntity::getRemaining));

        CredentialEntity entity = getRemaining.orElseThrow(() ->
                new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage()));

        Map<String, Object> payloadReq = dto.getPayload();
        String currency = payloadReq.getOrDefault(Static.CURRENCY, "").toString();
        String kind = payloadReq.getOrDefault(Static.KIND, "").toString();
        String filter = payloadReq.getOrDefault(Static.FILTER, "").toString();

        if(payloadReq.getOrDefault("test","false").toString().equals("true")) {
            String res = "{\n" +
                    "  \"status\": \"success\",\n" +
                    "  \"trace_id\": null,\n" +
                    "  \"message\": \"OK\",\n" +
                    "  \"date\": \"11-08-2025 09:32:07\",\n" +
                    "  \"data\": {\n" +
                    "    \"content\": {\n" +
                    "      \"next\": null,\n" +
                    "      \"previous\": null,\n" +
                    "      \"results\": [\n" +
                    "        {\n" +
                    "          \"id\": 24086393,\n" +
                    "          \"slug\": \"BNB-Drops-Below-800-USDT-with-a-106-Decrease-in-24-Hours\",\n" +
                    "          \"title\": \"BNB Drops Below 800 USDT with a 1.06% Decrease in 24 Hours\",\n" +
                    "          \"description\": \"On Aug 10, 2025, 07:44 AM(UTC). According to Binance Market Data, BNB has dropped below 800 USDT and is now trading at 798. 97998 USDT, with a narrowed 1. 06% decrease in 24 hours.\",\n" +
                    "          \"published_at\": \"2025-08-10T07:44:39Z\",\n" +
                    "          \"created_at\": \"2025-08-10T07:44:39+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24077774,\n" +
                    "          \"slug\": \"Ethena-ENA-soars-19-But-this-could-be-a-bull-trap-IF\",\n" +
                    "          \"title\": \"Ethena [ENA] soars 19% – But this could be a bull trap  IF…\",\n" +
                    "          \"description\": \"ENA’s rally is backed by strong inflows but hidden liquidity risks could flip momentum fast.\",\n" +
                    "          \"published_at\": \"2025-08-10T07:00:29Z\",\n" +
                    "          \"created_at\": \"2025-08-10T07:00:29+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24086384,\n" +
                    "          \"slug\": \"Ethenas-USDe-Surpasses-10-Billion-Market-Cap-in-500-Days\",\n" +
                    "          \"title\": \"Ethena's USDe Surpasses $10 Billion Market Cap in 500 Days\",\n" +
                    "          \"description\": \"According to PANews, Messari researcher Stablecoin Intern recently tweeted that Ethena's synthetic stablecoin, USDe, has achieved a market capitalization exceeding $10 billion in just 500 days.\",\n" +
                    "          \"published_at\": \"2025-08-10T03:49:07Z\",\n" +
                    "          \"created_at\": \"2025-08-10T03:49:07+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24086380,\n" +
                    "          \"slug\": \"BNB-Surpasses-810-USDT-with-a-174-Increase-in-24-Hours\",\n" +
                    "          \"title\": \"BNB Surpasses 810 USDT with a 1.74% Increase in 24 Hours\",\n" +
                    "          \"description\": \"On Aug 10, 2025, 03:19 AM(UTC). According to Binance Market Data, BNB has crossed the 810 USDT benchmark and is now trading at 810. 109985 USDT, with a narrowed 1. 74% increase in 24 hours.\",\n" +
                    "          \"published_at\": \"2025-08-10T03:19:37Z\",\n" +
                    "          \"created_at\": \"2025-08-10T03:19:37+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24086368,\n" +
                    "          \"slug\": \"BNB-Drops-Below-800-USDT-with-a-Narrowed-044-Increase-in-24-Hours\",\n" +
                    "          \"title\": \"BNB Drops Below 800 USDT with a Narrowed 0.44% Increase in 24 Hours\",\n" +
                    "          \"description\": \"On Aug 09, 2025, 22:37 PM(UTC). According to Binance Market Data, BNB has dropped below 800 USDT and is now trading at 799. 609985 USDT, with a narrowed narrowed 0. 44% increase in 24 hours.\",\n" +
                    "          \"published_at\": \"2025-08-09T22:37:37Z\",\n" +
                    "          \"created_at\": \"2025-08-09T22:37:37+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24066596,\n" +
                    "          \"slug\": \"Ethena-ENA-Surges-on-Whale-Accumulation-and-Massive-Buyback\",\n" +
                    "          \"title\": \"Ethena (ENA) Surges on Whale Accumulation and Massive Buyback\",\n" +
                    "          \"description\": \"The token&#8217;s market cap now stands at $4. 83 billion, with trading volume nearly doubling to $1.\",\n" +
                    "          \"published_at\": \"2025-08-09T14:20:12Z\",\n" +
                    "          \"created_at\": \"2025-08-09T14:20:12+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24064710,\n" +
                    "          \"slug\": \"BNB-Price-Prediction-Binance-Coin-Targeting-1200-As-Traders-Rush-Into-Layer-1s-Again\",\n" +
                    "          \"title\": \"BNB Price Prediction: Binance Coin Targeting $1,200 As Traders Rush Into Layer 1s Again\",\n" +
                    "          \"description\": \"BNB price prediction is making waves again as analysts target $1,200 for Q4 2025. With renewed interest in Layer 1 tokens and institutional capital flowing back into utility-rich ecosystems, Binance Coin is positioning itself as a top crypto to buy now.\",\n" +
                    "          \"published_at\": \"2025-08-09T12:30:29Z\",\n" +
                    "          \"created_at\": \"2025-08-09T12:30:29+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24063534,\n" +
                    "          \"slug\": \"As-Ethereum-rips-and-smaller-caps-follow-is-alt-season-finally-here\",\n" +
                    "          \"title\": \"As Ethereum rips and smaller caps follow, is alt season finally here?\",\n" +
                    "          \"description\": \"Altcoin traders can hardly contain themselves as the long-awaited breakout appears to have begun. ETH posted gains of over 20% the last seven days and smaller cap Coins like SOL, DOGE, and BNB are close behind, with Chainlink’s LINK token up 30% in the same timeframe.\",\n" +
                    "          \"published_at\": \"2025-08-09T11:41:55Z\",\n" +
                    "          \"created_at\": \"2025-08-09T11:41:55+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24064089,\n" +
                    "          \"slug\": \"BNB-Market-Cap-Climbs-to-11285B-Overtaking-Strategy-and-DBS-Bank\",\n" +
                    "          \"title\": \"BNB Market Cap Climbs to $112.85B, Overtaking Strategy and DBS Bank\",\n" +
                    "          \"description\": \"BNB’s market capitalization has surged to $112. 85 billion, marking a 7. 72% gain over the past seven days, according to data from 8marketcap. The rally pushed BNB ahead of several major traditional finance and corporate players, including DBS Bank, Strategy, and Welltower, securing the 191st position in the global asset market cap rankings.\",\n" +
                    "          \"published_at\": \"2025-08-09T11:03:00Z\",\n" +
                    "          \"created_at\": \"2025-08-09T11:03:00+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24062591,\n" +
                    "          \"slug\": \"Trumps-World-Liberty-Financial-To-Invest-Billions-Into-Crypto-Treasury-Firms\",\n" +
                    "          \"title\": \"Trump’s World Liberty Financial To Invest Billions Into Crypto Treasury Firms\",\n" +
                    "          \"description\": \"The Trump family’s DeFi project, World Liberty Financial, is preparing an ambitious $1. 5 billion push into the fast-growing market for crypto treasury firms. Crypto-based treasury remains a growing trend, with several ecosystems, including Ethereum, BNB, Dogecoin, and PENGU, already in the fold. Trump-Linked World Liberty Financial Targets Public Market Debut The plan involves creating a publicly listed company holding the venture’s WLFI tokens\",\n" +
                    "          \"published_at\": \"2025-08-09T10:09:27Z\",\n" +
                    "          \"created_at\": \"2025-08-09T10:09:27+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24064070,\n" +
                    "          \"slug\": \"BNB-Surpasses-810-USDT-with-a-319-Increase-in-24-Hours\",\n" +
                    "          \"title\": \"BNB Surpasses 810 USDT with a 3.19% Increase in 24 Hours\",\n" +
                    "          \"description\": \"According to Binance Market Data, BNB crossed the 810 USDT benchmark and is now trading at 810 USDT, with a 3. 19% increase in 24 hours.\",\n" +
                    "          \"published_at\": \"2025-08-09T09:37:38Z\",\n" +
                    "          \"created_at\": \"2025-08-09T09:37:38+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24058902,\n" +
                    "          \"slug\": \"XRP-BNB-BTC-Have-Done-It-Is-It-ETHs-Time-to-Shine-Now\",\n" +
                    "          \"title\": \"XRP, BNB, BTC Have Done It: Is It ETH’s Time to Shine Now?\",\n" +
                    "          \"description\": \"ETH is less than 15% away from a new all-time high.\",\n" +
                    "          \"published_at\": \"2025-08-09T07:21:06Z\",\n" +
                    "          \"created_at\": \"2025-08-09T07:21:06+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24061181,\n" +
                    "          \"slug\": \"BNB-Surpasses-800-USDT-with-a-165-Increase-in-24-Hours\",\n" +
                    "          \"title\": \"BNB Surpasses 800 USDT with a 1.65% Increase in 24 Hours\",\n" +
                    "          \"description\": \"According to Binance Market Data, BNB crossed the 800 USDT benchmark and is now trading at 800 USDT, with a 1. 65% increase in 24 hours.\",\n" +
                    "          \"published_at\": \"2025-08-09T04:37:37Z\",\n" +
                    "          \"created_at\": \"2025-08-09T04:37:37+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24054148,\n" +
                    "          \"slug\": \"Pendle-Blasts-Past-5-as-TVL-Hits-Records-and-New-Integrations-Fuel-Rally\",\n" +
                    "          \"title\": \"Pendle Blasts Past $5 as TVL Hits Records and New Integrations Fuel Rally\",\n" +
                    "          \"description\": \"Pendle price breaks $5 as TVL hits $7. 8B with new integrations and features driving strong liquidity and fueling a fresh DeFi rally. Pendle’s price recently jumped above $5, signaling strong momentum for the DeFi token. The project’s total value locked (TVL) has reached record highs amid expanding user activity\",\n" +
                    "          \"published_at\": \"2025-08-09T03:43:49Z\",\n" +
                    "          \"created_at\": \"2025-08-09T03:43:49+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24047923,\n" +
                    "          \"slug\": \"Ripples-RLUSD-stablecoin-hits-600M-supply-milestone-in-July\",\n" +
                    "          \"title\": \"Ripple’s RLUSD stablecoin hits $600M supply milestone in July\",\n" +
                    "          \"description\": \"Ripple’s stablecoin RLUSD grew 32. 3% in supply between June and July, surpassing $600 million. This was the second most significant growth in supply among stablecoins with over $500 million in supply growth, lagging only Ethena Labs’ USDe, which grew 63. 4% in the same period\",\n" +
                    "          \"published_at\": \"2025-08-08T21:15:02Z\",\n" +
                    "          \"created_at\": \"2025-08-08T21:15:02+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24042014,\n" +
                    "          \"slug\": \"Price-predictions-88-BTC-ETH-XRP-BNB-SOL-DOGE-ADA-HYPE-XLM-SUI\",\n" +
                    "          \"title\": \"Price predictions 8/8: BTC, ETH, XRP, BNB, SOL, DOGE, ADA, HYPE, XLM, SUI\",\n" +
                    "          \"description\": \"Bitcoin could challenge the $120,000 to $123,218 resistance zone but crossing it may be a tough ask for the bulls.\",\n" +
                    "          \"published_at\": \"2025-08-08T16:11:26Z\",\n" +
                    "          \"created_at\": \"2025-08-08T16:11:26+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24040981,\n" +
                    "          \"slug\": \"BNB-Market-Cap-Surpasses-Nikes-Valuation\",\n" +
                    "          \"title\": \"BNB Market Cap Surpasses Nike's Valuation\",\n" +
                    "          \"description\": \"According to Foresight News, data from 8marketcap indicates that BNB's market capitalization has reached $110 billion, surpassing Nike's valuation of $109. 79 billion. This development highlights the growing influence and financial strength of BNB in the global market.\",\n" +
                    "          \"published_at\": \"2025-08-08T14:53:35Z\",\n" +
                    "          \"created_at\": \"2025-08-08T14:53:35+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24038940,\n" +
                    "          \"slug\": \"BNB-Surpasses-790-USDT-with-a-198-Increase-in-24-Hours\",\n" +
                    "          \"title\": \"BNB Surpasses 790 USDT with a 1.98% Increase in 24 Hours\",\n" +
                    "          \"description\": \"On Aug 08, 2025, 13:45 PM(UTC). According to Binance Market Data, BNB has crossed the 790 USDT benchmark and is now trading at 790. 700012 USDT, with a narrowed 1. 98% increase in 24 hours.\",\n" +
                    "          \"published_at\": \"2025-08-08T13:45:43Z\",\n" +
                    "          \"created_at\": \"2025-08-08T13:45:43+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24037945,\n" +
                    "          \"slug\": \"Behind-the-scenes-of-public-companies-that-are-rushing-to-create-crypto-treasuries\",\n" +
                    "          \"title\": \"Behind the scenes of public companies that are rushing to create crypto treasuries\",\n" +
                    "          \"description\": \"Publicly traded companies are building strategic reserves in digital assets like BNB and Solana. Industry leaders explain why this could be the next institutional on-ramp for crypto.\",\n" +
                    "          \"published_at\": \"2025-08-08T13:30:00Z\",\n" +
                    "          \"created_at\": \"2025-08-08T13:30:00+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 24037727,\n" +
                    "          \"slug\": \"Whales-Scoop-26-Million-in-PENDLE-as-USDe-Loop-Goes-LiveDip-First-or-Breakout\",\n" +
                    "          \"title\": \"Whales Scoop $2.6 Million in PENDLE as USDe Loop Goes Live—Dip First or Breakout?\",\n" +
                    "          \"description\": \"Pendle (PENDLE) has rallied over 27% in the past 24 hours, following its new integration with Ethena (ENA). The collaboration enables a high-yield strategy using Pendle’s principal tokens and Aave’s borrowing markets, allowing users to loop stablecoins like USDe for fixed yields as high as 8. 8%. As capital flooded into the new pools, both ENA and PENDLE surged, pulling in whales and traders\",\n" +
                    "          \"published_at\": \"2025-08-08T12:05:11Z\",\n" +
                    "          \"created_at\": \"2025-08-08T12:05:11+00:00\",\n" +
                    "          \"kind\": \"news\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    \"status\": null\n" +
                    "  }\n" +
                    "}";
            ApiResponder resultBuilder = new ApiResponder();
            ObjectMapper mapper = new ObjectMapper();

            resultBuilder.setContent(mapper.readValue(res, DynamicResponse.class));
            credentialService.consume(providerName);

            return ResponseBuilder.success(resultBuilder);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Static.CRYPTO_PANIC_BASE_URL)
                .queryParam(Static.AUTH_TOKEN, entity.getApiKey());

        if (!kind.isEmpty() && AVAILABLE_KIND.contains(kind))
            builder.queryParam(Static.KIND, kind);

        if(!currency.isEmpty())
            builder.queryParam(Static.CURRENCIES, currency);

        if(!filter.isEmpty() && AVAILABLE_FILTER.contains(filter))
                builder.queryParam(Static.FILTER, filter);

        URI fullUri = builder.build().encode().toUri();

        log.info("Calling external API with URL: {}", fullUri);

        RestTemplateHelper client = new RestTemplateHelper();
        String result = client.doGet(fullUri.toString(), null, null, String.class);

        ApiResponder resultBuilder = new ApiResponder();
        ObjectMapper mapper = new ObjectMapper();

        resultBuilder.setContent(mapper.readValue(result, DynamicResponse.class));
        credentialService.consume(providerName);

        return ResponseBuilder.success(resultBuilder);
    }

}
