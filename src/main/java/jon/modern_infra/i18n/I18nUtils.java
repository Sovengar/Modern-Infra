package jon.modern_infra.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class I18nUtils {
    private final MessageSource messageSource;

    public String getMessage(String message) {
        return this.messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String message, String[] params) {
        return this.messageSource.getMessage(message, params, LocaleContextHolder.getLocale());
    }

    public String getMessage(String message, Locale locale) {
        return this.messageSource.getMessage(message, null, getSpecificLocale(locale));
    }

    public String getMessage(String message, String[] params, Locale locale) {
        return this.messageSource.getMessage(message, params, getSpecificLocale(locale));
    }

    private Locale getSpecificLocale(Locale locale) {
        if (!locale.getCountry().isEmpty()) {
            return locale;
        }

        if ("ca".equals(locale.getLanguage()) || "es".equals(locale.getLanguage())) {
            return new Locale(locale.getLanguage(), "ES");
        }

        return locale;
    }
}
