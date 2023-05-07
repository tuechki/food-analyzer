package bg.sofia.uni.fmi.mjt.foodanalyzer;


import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodNutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Nutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.NutrientName;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.Unit;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FDCClientTest {
    private static final String FDC_ID = "666666";
    private static final String QUERY = "query for food";

    @Mock
    private HttpClient fdcHttpClientMock;

    @Mock
    private HttpResponse httpResponse;

    @InjectMocks
    private FDCClient fdcClientMock;


    //--------------------------- Food Report ---------------------------------
    @Test
    public void testGetFoodReportThrowsBadRequestExceptionWhenStatusCode400()
        throws IOException, InterruptedException {

        mockFdcHttpClientForStatusCode(400);

        assertThrows(BadRequestException.class, () -> fdcClientMock.getFoodReport(FDC_ID),
            "BadRequestException expected to be thrown when API Service respond with status code 400.");
    }

    @Test
    public void testGetFoodReportThrowsApiKeyMissingExceptionWhenStatusCode401()
        throws IOException, InterruptedException {

        mockFdcHttpClientForStatusCode(401);

        assertThrows(ApiKeyMissingException.class, () -> fdcClientMock.getFoodReport(FDC_ID),
            "ApiKeyMissingException expected to be thrown when API Service respond with status code 401.");
    }


    @Test
    public void testGetFoodReportThrowsBadRequestExceptionWhenStatusCode404()
        throws IOException, InterruptedException {

        mockFdcHttpClientForStatusCode(404);

        assertThrows(NoResultsFoundException.class, () -> fdcClientMock.getFoodReport(FDC_ID),
            "NoResultsFoundException expected to be thrown when API Service respond with status code 404.");
    }


    @Test
    public void testGetFoodReportThrowsFoodRetrievalExceptionWhenCommunicationErrorOccur()
        throws IOException, InterruptedException {

        when(fdcHttpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
            .thenThrow(new IllegalArgumentException("Request argument could not have been validly built."));

        assertThrows(FoodRetrievalClientException.class, () -> fdcClientMock.getFoodReport(FDC_ID),
            "FoodRetrievalClientException expected to be thrown " +
                                                        "when IllegalArgumentException occur from API Service.");
    }


    @Test
    public void testGetFoodReportReturnObjectWhenWhenStatusCode200()
        throws IOException, InterruptedException, ApiKeyMissingException, FoodRetrievalClientException,
        NoResultsFoundException, BadRequestException, URISyntaxException {

        mockFdcHttpClientForStatusCode(200);
        when(httpResponse.body()).thenReturn(getFoodReportResponseBody());

        assertEquals(getFoodReport(), fdcClientMock.getFoodReport(FDC_ID),
            "When the API Service return a particular JSON, the right object is created on the server.");
    }


    @Test
    public void testGetFoodReportThrowsFoodRetrievalExceptionWhenStatusCodeNotProcessed()
        throws Throwable {

        mockFdcHttpClientForStatusCode(407);

        assertThrows(FoodRetrievalClientException.class, () -> fdcClientMock.getFoodReport(FDC_ID),
            "FoodRetrievalClientException expected to be thrown when unknown status code is returned.");
    }


    //---------------------------- Foods --------------------------------------

    @Test
    public void testGetFoodsThrowsBadRequestExceptionWhenStatusCode400()
        throws IOException, InterruptedException {

        mockFdcHttpClientForStatusCode(400);

        assertThrows(BadRequestException.class, () -> fdcClientMock.getFoods(QUERY),
            "BadRequestException expected to be thrown when API Service respond with status code 400.");
    }

    @Test
    public void testGetFoodsThrowsApiKeyMissingExceptionWhenStatusCode401()
        throws IOException, InterruptedException {

        mockFdcHttpClientForStatusCode(401);

        assertThrows(ApiKeyMissingException.class, () -> fdcClientMock.getFoods(QUERY),
            "ApiKeyMissingException expected to be thrown when API Service respond with status code 401.");
    }


    @Test
    public void testGetFoodsThrowsFoodRetrievalExceptionWhenCommunicationErrorOccur()
        throws IOException, InterruptedException {

        when(fdcHttpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
            .thenThrow(new IllegalArgumentException("Request argument could not have been validly built."));

        assertThrows(FoodRetrievalClientException.class, () -> fdcClientMock.getFoods(QUERY),
            "FoodRetrievalClientException expected to be thrown " +
                "when IllegalArgumentException occur from API Service.");
    }


    @Test
    public void testGetFoodsReturnObjectWhenWhenStatusCode200()
        throws IOException, InterruptedException, ApiKeyMissingException, FoodRetrievalClientException,
        NoResultsFoundException, BadRequestException, URISyntaxException {

        mockFdcHttpClientForStatusCode(200);
        when(httpResponse.body()).thenReturn(getFoodResponseBody());

        assertIterableEquals(getFoods(), fdcClientMock.getFoods(QUERY),
            "When the API Service return a particular JSON, the right object is created on the server.");
    }


    @Test
    public void testGetFoodsThrowsFoodRetrievalExceptionWhenStatusCodeNotProcessed()
        throws Throwable {

        mockFdcHttpClientForStatusCode(407);

        assertThrows(FoodRetrievalClientException.class, () -> fdcClientMock.getFoods(QUERY),
            "FoodRetrievalClientException expected to be thrown when unknown status code is returned.");
    }

    private void mockFdcHttpClientForStatusCode(int statusCode) throws IOException, InterruptedException {
        when(httpResponse.statusCode()).thenReturn(statusCode);
        when(fdcHttpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
            .thenReturn(httpResponse);
    }




    private static FoodReport getFoodReport() {
        Nutrient proteinNutrient = new Nutrient(NutrientName.PROTEIN.getName(), Unit.GRAM.getName());
        Nutrient lipidNutrient = new Nutrient(NutrientName.LIPID.getName(), Unit.GRAM.getName());
        Nutrient carbohydrateNutrient = new Nutrient(NutrientName.CARBOHYDRATE.getName(), Unit.GRAM.getName());
        Nutrient fiberNutrient = new Nutrient(NutrientName.FIBER.getName(), Unit.GRAM.getName());
        Nutrient energyNutrient = new Nutrient(NutrientName.ENERGY.getName(), Unit.KCAL.getName());

        FoodNutrient protein = new FoodNutrient(proteinNutrient, "6.67000000");
        FoodNutrient lipid = new FoodNutrient(lipidNutrient, "50.00000000");
        FoodNutrient carbohydrate = new FoodNutrient(carbohydrateNutrient, "40.00000000");
        FoodNutrient fiber = new FoodNutrient(fiberNutrient, "3.30000000");
        FoodNutrient energy = new FoodNutrient(energyNutrient, "633.00000000");


        return FoodReport.builder("RAFFAELLO, ALMOND COCONUT TREAT")
            .setIngredients("VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.")
            .setFoodNutrients(new FoodNutrient[] {protein, lipid, carbohydrate, fiber, energy})
            .build();
    }

    private static List<Food> getFoods() {
        Food food = new Food("RAFFAELLO, ALMOND COCONUT TREAT", "2041155", "009800146130");

        return List.of(food);
    }

    private static String getFoodReportResponseBody() {
        return """
            {
               "description":"RAFFAELLO, ALMOND COCONUT TREAT",
               "ingredients":"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.",
               "foodNutrients":[
                  {
                     "nutrient":{
                        "name":"Protein",
                        "unitName":"g"
                     },
                     "amount":"6.67000000"
                  },
                  {
                     "nutrient":{
                        "name":"Total lipid (fat)",
                        "unitName":"g"
                     },
                     "amount":"50.00000000"
                  },
                  {
                     "nutrient":{
                        "name":"Energy",
                        "unitName":"kcal"
                     },
                     "amount":"633.00000000"
                  },
                  {
                     "nutrient":{
                        "name":"Carbohydrate, by difference",
                        "unitName":"g"
                     },
                     "amount":"40.00000000"
                  },
                  {
                     "nutrient":{
                        "name":"Fiber, total dietary",
                        "unitName":"g"
                     },
                     "amount":"3.30000000"
                  }
               ]
            }
            """;
    }

    private static String getFoodResponseBody() {
        return """
            {
               "foods":[
                  {
                     "description":"RAFFAELLO, ALMOND COCONUT TREAT",
                     "fdcId":"2041155",
                     "gtinUpc":"009800146130"
                  }
               ]
            }
            """;
    }







    //        private static Set<Arguments> provideFdcClientMethods() {
//            return Set.of(
//                Arguments.of(new Executable() {
//                    @Override
//                    public void execute() throws Throwable {
//                        fdcClientMock.getFoodReport(FDC_ID);
//                    }
//                }),
//                Arguments.of(new Executable() {
//                    @Override
//                    public void execute() throws Throwable {
//                        fdcClientMock.getFood(FDC_ID);
//                    }
//                })
//            );
//        }
//
//    @ParameterizedTest
//    @MethodSource("provideFdcClientMethods")






    @Test
    public void testGetFoodReportValid() throws URISyntaxException, IOException, InterruptedException {

//        HttpResponse<String> response = mock(HttpResponse.class);
//        when(response.statusCode()).thenReturn(401);
//
//        String fdcId = "666666";
//
//        URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_REPORT_ENDPOINT_PATH + fdcId,
//            "nutrients=203&" +
//                "nutrients=204&" +
//                "nutrients=205&" +
//                "nutrients=291&" +
//                "nutrients=208&" +
//                "api_key=" + API_KEY, null);
//        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
//        when(fdcHttpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
//            .thenReturn(response);
//
//        assertThrows(ApiKeyMissingException.class, () -> fdcClientMock.getFoodReport(fdcId),
//            "ApiKeyMissingException expected to be thrown when API Service respond with status code 401.");

//        assertEquals(11.1, exchange.exchangeSum(Currency.EUR, Currency.USD, 10), 0.001,
//            "Currency exchange of EUR to USD should work properly");
//
//        verify(currencyConverterMock).getExchangeRate(Currency.EUR, Currency.USD);
//        verify(currencyConverterMock, times(1)).getExchangeRate(Currency.EUR, Currency.USD);
        // uncomment the next line to play with mock verification failure
        //verify(currencyConverterMock).getExchangeRate(Currency.EUR, Currency.BGN);
    }

//    @Test
//    public void testCurrencyExchangeUnknownCurrencyThrowsException() throws UnknownCurrencyException {
//
//        when(currencyConverterMock.getExchangeRate(Currency.BGN, Currency.USD))
//            .thenThrow(new UnknownCurrencyException(
//                String.format("Unknown currency pair (%s --> %s)", Currency.BGN, Currency.USD)));
//
//        assertThrows(UnknownCurrencyException.class, () -> exchange.exchangeSum(Currency.BGN, Currency.USD, 10),
//            "UnknownCurrencyException expected to be thrown when converting between currencies one of which is unknown");
//    }




}
