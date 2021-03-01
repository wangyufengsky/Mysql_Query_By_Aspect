package mysqlAspect.utils;



import lombok.extern.slf4j.Slf4j;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class LambdaUtils {


    public static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                log.error("Exception occured : " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        };
    }


    public static <T,R> Function<T,R> throwingFunctionWrapper(ThrowingFunction<T,R,Exception> function) {
        return i -> {
            try {
                return function.apply(i);
            } catch (Exception ex) {
                log.error("Exception occured : " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T,U> BiConsumer<T,U> throwingBiConsumerWrapper(ThrowingBiConsumer<T,U,Exception> biConsumer) {
        return (k,v) -> {
            try {
                biConsumer.accept(k,v);
            } catch (Exception ex) {
                log.error("Exception occured : " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        };
    }
}
