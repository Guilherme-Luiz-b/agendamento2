package br.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * Class auxiliar para conversões referente a data
 */
public class DateConverter {
    private static final Logger LOG = LoggerFactory.getLogger(DateConverter.class);
    private static final String DATE_TIME_US_PATTERN = "yyyy-MM-dd HH:mm";
    private static final String DATE_US_PATTERN = "yyyy/MM/dd";
    private static final String DATE_TIME_BR_PATTERN = "dd/MM/yyyy HH:mm";
    private static final String DATE_BR_PATTERN = "dd/MM/yyyy";
    private static final String DATE_SAP = "yyyyMMdd";
    private static final String TIME_SAP = "HHmmss";

    /**
     * Obtém o primeiro dia do mês para a data fornecida.
     *
     * @param data A data para a qual deseja-se obter o primeiro dia do mês.
     * @return Um objeto LocalDateTime representando o primeiro dia do mês da data fornecida.
     */
    public static LocalDateTime getPrimeiroDiaDoMes(LocalDateTime data) {
        return LocalDateTime.of(data.getYear(), data.getMonth(), 1, 0, 0);
    }

    /**
     * Obtém o último dia do mês para a data fornecida.
     *
     * @param data A data para a qual deseja-se obter o último dia do mês.
     * @return Um objeto LocalDateTime representando o último dia do mês da data fornecida.
     */
    public static LocalDateTime getUltimoDiaDoMes(LocalDateTime data) {
        return LocalDateTime.of(data.getYear(), data.getMonth(), 1, 23, 59).with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Converte um objeto LocalDate para um objeto Date.
     *
     * @param localDate O objeto LocalDate a ser convertido para Date.
     * @return Um objeto Date representando a mesma data que o LocalDate fornecido.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Date localDateToDate(LocalDate localDate) {
        try {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto LocalDateTime para um objeto Date.
     *
     * @param localDate O objeto LocalDateTime a ser convertido para Date.
     * @return Um objeto Date representando a mesma data e hora que o LocalDateTime fornecido.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Date localDateTimeToDate(LocalDateTime localDate) {
        try {
            return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto LocalDateTime para uma representação de String no formato brasileiro.
     *
     * @param date O objeto LocalDateTime a ser convertido para String.
     * @return Uma String representando a data e hora no formato brasileiro (dd/MM/yyyy HH:mm).
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String localDateTimeToStringBr(LocalDateTime date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_BR_PATTERN);
            return date.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato brasileiro para um objeto LocalDateTime.
     *
     * @param string A String a ser convertida para LocalDateTime, no formato brasileiro (dd/MM/yyyy HH:mm).
     * @return Um objeto LocalDateTime representando a data e hora da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDateTime stringTimeBrToLocalDateTime(String string) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_BR_PATTERN);
            return LocalDateTime.parse(string, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto LocalDate para uma representação de String usando o formato fornecido.
     *
     * @param date    O objeto LocalDate a ser convertido para String.
     * @param formato O formato desejado para a representação da data (por exemplo, "dd/MM/yyyy").
     * @return Uma String representando a data no formato especificado.
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String localDateToString(LocalDate date, String formato) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
            return date.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto LocalDateTime para uma representação de String usando o formato fornecido.
     *
     * @param date    O objeto LocalDateTime a ser convertido para String.
     * @param formato O formato desejado para a representação da data e hora (por exemplo, "dd/MM/yyyy HH:mm").
     * @return Uma String representando a data e hora no formato especificado.
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String localDateTimeToString(LocalDateTime date, String formato) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
            return date.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String para um objeto LocalDate usando o formato fornecido.
     *
     * @param data    A String a ser convertida para LocalDate.
     * @param formato O formato desejado para a representação da data (por exemplo, "dd/MM/yyyy").
     * @return Um objeto LocalDate representando a data da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDate stringToLocalDate(String data, String formato) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
            return LocalDate.parse(data, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String para um objeto LocalDateTime usando o formato fornecido.
     *
     * @param data    A String a ser convertida para LocalDateTime.
     * @param formato O formato desejado para a representação da data e hora (por exemplo, "dd/MM/yyyy HH:mm").
     * @return Um objeto LocalDateTime representando a data e hora da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDateTime stringToLocalDateTime(String data, String formato) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
            return LocalDateTime.parse(data, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato brasileiro para um objeto LocalDate.
     *
     * @param string A String a ser convertida para LocalDate, no formato brasileiro (dd/MM/yyyy).
     * @return Um objeto LocalDate representando a data da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDate stringBrToLocalDate(String string) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_BR_PATTERN);
            return LocalDate.parse(string, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto LocalDateTime para uma representação de String no formato padrão dos Estados Unidos.
     *
     * @param date O objeto LocalDateTime a ser convertido para String.
     * @return Uma String representando a data e hora no formato padrão dos Estados Unidos (MM/dd/yyyy HH:mm).
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String localDateTimeToString(LocalDateTime date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_US_PATTERN);
            return date.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato de tempo para um objeto LocalDateTime.
     *
     * @param string A String a ser convertida para LocalDateTime, no formato de tempo (substituindo "T" por espaço).
     * @return Um objeto LocalDateTime representando a data e hora da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDateTime stringTimeToLocalDateTime(String string) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_US_PATTERN);
            return LocalDateTime.parse(string.replace("T", " "), formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato brasileiro para um objeto Calendar.
     *
     * @param string A String a ser convertida para Calendar, no formato brasileiro (dd/MM/yyyy HH:mm).
     * @return Um objeto Calendar representando a data e hora da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Calendar stringTimeBrToCalendar(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_BR_PATTERN);
        Date date;
        try {
            date = sdf.parse(string);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converte uma String para um objeto Calendar usando o padrão fornecido.
     *
     * @param string  A String a ser convertida para Calendar.
     * @param pattern O padrão desejado para a representação da data e hora na String.
     * @return Um objeto Calendar representando a data e hora da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Calendar stringToCalendar(String string, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = sdf.parse(string);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato brasileiro para um objeto Calendar.
     *
     * @param string A String a ser convertida para Calendar, no formato brasileiro (dd/MM/yyyy).
     * @return Um objeto Calendar representando a data da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Calendar stringBrToCalendar(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_BR_PATTERN);
        Date date;
        try {
            date = sdf.parse(string);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converte um objeto Calendar para uma representação de String no formato de tempo dos Estados Unidos.
     *
     * @param calendar O objeto Calendar a ser convertido para String.
     * @return Uma String representando a data e hora no formato padrão dos Estados Unidos (MM/dd/yyyy HH:mm).
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String calendarToStringTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_US_PATTERN);
        try {
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato de tempo dos Estados Unidos para um objeto Calendar.
     *
     * @param string A String a ser convertida para Calendar, no formato de tempo (MM/dd/yyyy HH:mm).
     * @return Um objeto Calendar representando a data e hora da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Calendar stringTimeToCalendar(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_US_PATTERN);
        Date date;
        try {
            date = sdf.parse(string);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converte um objeto Calendar para uma representação de String no formato brasileiro.
     *
     * @param calendar O objeto Calendar a ser convertido para String.
     * @return Uma String representando a data no formato brasileiro (dd/MM/yyyy).
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String calendarToStringBr(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_BR_PATTERN);
        try {
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto Calendar para uma representação de String usando o formato fornecido.
     *
     * @param calendar O objeto Calendar a ser convertido para String.
     * @param formato  O formato desejado para a representação da data e hora na String.
     * @return Uma String representando a data e hora no formato especificado.
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String calendarToString(Calendar calendar, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        try {
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto Date para uma representação de String usando o formato fornecido.
     *
     * @param date    O objeto Date a ser convertido para String.
     * @param formato O formato desejado para a representação da data e hora na String.
     * @return Uma String representando a data e hora no formato especificado.
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String dateToString(Date date, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        try {
            return sdf.format(date.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formata um objeto Date para uma representação de String no formato utilizado pelo sistema SAP.
     *
     * @param data O objeto Date a ser formatado.
     * @return Uma String representando a data no formato SAP (MM-dd-yyyy) sem barras ("/").
     */
    public static String formatToSAP(final Date data) {
        return new SimpleDateFormat(DATE_US_PATTERN).format(data).replace("/", "");
    }

    /**
     * Formata um objeto LocalDate para uma representação de String no formato utilizado pelo sistema SAP.
     *
     * @param data O objeto LocalDate a ser formatado.
     * @return Uma String representando a data no formato SAP (MM-dd-yyyy) sem barras ("/").
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String formatToSAP(final LocalDate data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_US_PATTERN);
            return data.format(formatter).replace("/", "");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formata um objeto LocalDateTime para uma representação de String no formato utilizado pelo sistema SAP.
     *
     * @param data O objeto LocalDateTime a ser formatado.
     * @return Uma String representando a data no formato SAP (MM-dd-yyyy) sem barras ("/").
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String formatToSAP(final LocalDateTime data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_US_PATTERN);
            return data.format(formatter).replace("/", "");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formata um objeto Date para uma representação de String no formato de hora utilizado pelo sistema SAP.
     *
     * @param data O objeto Date a ser formatado.
     * @return Uma String representando a hora no formato SAP (HH:mm:ss).
     */
    public static String formatHora(Date data) {
        return new SimpleDateFormat(TIME_SAP).format(data);
    }

    /**
     * Formata um objeto LocalDate para uma representação de String no formato de hora utilizado pelo sistema SAP.
     *
     * @param data O objeto LocalDate a ser formatado.
     * @return Uma String representando a hora no formato SAP (HH:mm:ss).
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String formatHora(final LocalDate data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_SAP);
            return data.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formata um objeto LocalDateTime para uma representação de String no formato de hora utilizado pelo sistema SAP.
     *
     * @param data O objeto LocalDateTime a ser formatado.
     * @return Uma String representando a hora no formato SAP (HH:mm:ss).
     * Retorna null em caso de exceção durante a formatação.
     */
    public static String formatHora(final LocalDateTime data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_SAP);
            return data.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato SAP para um objeto LocalDateTime.
     *
     * @param string no formato SAP (yyyy-MM-dd'T'HH:mm:ss) a ser convertida.
     * @return Um LocalDateTime representando a data e hora da String fornecida.
     * Retorna null em caso de erro na conversão.
     */
    public static LocalDateTime obterLocalDateTimeFormatoSAP(String string) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_SAP);
            return LocalDateTime.parse(string, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato SAP para um objeto LocalDate.
     *
     * @param string A String a ser convertida para LocalDate, no formato SAP (yyyy-MM-dd).
     * @return Um objeto LocalDate representando a data da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDate obterLocalDateFormatoSAP(String string) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_SAP);
            return LocalDate.parse(string, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte uma String no formato SAP para um objeto Calendar.
     *
     * @param string A String a ser convertida para Calendar, no formato SAP (yyyy-MM-dd).
     * @return Um objeto Calendar representando a data da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Calendar obterDataFormatoSAP(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_SAP);
        Date date;
        try {
            date = sdf.parse(string);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converte um objeto Date para um objeto Calendar.
     *
     * @param date O objeto Date a ser convertido para Calendar.
     * @return Um objeto Calendar representando a data e hora do objeto Date fornecido.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Calendar dateToCalendar(Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto Date para um objeto LocalDateTime.
     *
     * @param date O objeto Date a ser convertido para LocalDateTime.
     * @return Um objeto LocalDateTime representando a data e hora do objeto Date fornecido.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        try {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte um objeto Date para um objeto LocalDate.
     *
     * @param date O objeto Date a ser convertido para LocalDate.
     * @return Um objeto LocalDate representando a data do objeto Date fornecido.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDate dateToLocalDate(Date date) {
        try {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Calcula a diferença em dias entre duas datas representadas por objeto Calendar.
     *
     * @param date1 O primeiro objeto Calendar.
     * @param date2 O segundo objeto Calendar.
     * @return Um Integer representando a diferença em dias entre as duas datas.
     * Retorna null em caso de exceção durante o cálculo.
     */
    public static Integer getDiferencaDias(Calendar date1, Calendar date2) {
        return (int) ChronoUnit.DAYS.between(date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * Calcula a diferença em dias entre duas datas representadas por objetos LocalDate.
     *
     * @param date1 O primeiro objeto LocalDate.
     * @param date2 O segundo objeto LocalDate.
     * @return Um Integer representando a diferença em dias entre as duas datas.
     * Retorna null em caso de exceção durante o cálculo.
     */
    public static Integer getDiferencaDias(LocalDate date1, LocalDate date2) {
        return (int) ChronoUnit.DAYS.between(date1.atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDate(), date2.atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * Calcula a diferença em dias entre duas datas representadas por objetos LocalDateTime.
     *
     * @param date1 O primeiro objeto LocalDateTime.
     * @param date2 O segundo objeto LocalDateTime.
     * @return Um Integer representando a diferença em dias entre as duas datas.
     * Retorna null em caso de exceção durante o cálculo.
     */
    public static Integer getDiferencaDias(LocalDateTime date1, LocalDateTime date2) {
        return (int) ChronoUnit.DAYS.between(date1.atZone(ZoneId.systemDefault()).toLocalDate(), date2.atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * Calcula a diferença em meses entre duas datas representadas por objetos LocalDate.
     *
     * @param date1 O primeiro objeto LocalDate.
     * @param date2 O segundo objeto LocalDate.
     * @return Um Integer representando a diferença em meses entre as duas datas.
     * Retorna null em caso de exceção durante o cálculo.
     */
    public static Integer getDiferencaMeses(LocalDate date1, LocalDate date2) {
        return (int) ChronoUnit.MONTHS.between(date1.atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDate(), date2.atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * Obtém o ano referente a uma safra representada por uma String no formato "YYYY/YYYY".
     *
     * @param safra A String que representa a safra no formato "YYYY/YYYY".
     * @return Um Integer representando o ano referente à safra.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Integer anoBySafra(String safra) {
        String[] s = safra.split("/");
        return Integer.valueOf(s[1]);
    }

    /**
     * Converte uma String para um objeto Date usando o formato especificado.
     *
     * @param data    A String a ser convertida para Date.
     * @param formato O formato desejado para a conversão da String para Date.
     * @return Um objeto Date representando a data da String fornecida.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static Date stringToDate(String data, String formato) {
        try {
            return new SimpleDateFormat(formato).parse(data);
        } catch (ParseException e) {
            String metodo = Object.class.getEnclosingMethod().getName();
            String classe = Object.class.getName();
            LOG.error("{} | {} | {} | {}", classe, metodo, e.getStackTrace()[0].getLineNumber(), e.getMessage());
            return null;
        }
    }

    /**
     * Obtém o nome do mês com base no valor numérico do mês.
     *
     * @param monthValue O valor numérico do mês.
     * @return Uma String representando o nome do mês correspondente ao valor numérico fornecido.
     * Retorna null se o valor numérico do mês for inválido.
     */
    public static String getMonthByNumber(int monthValue) {
        return switch (monthValue) {
            case 1 -> "Janeiro";
            case 2 -> "Fevereiro";
            case 3 -> "Março";
            case 4 -> "Abril";
            case 5 -> "Maio";
            case 6 -> "Junho";
            case 7 -> "Julho";
            case 8 -> "Agosto";
            case 9 -> "Setembro";
            case 10 -> "Outubro";
            case 11 -> "Novembro";
            case 12 -> "Dezembro";
            default -> null;
        };
    }

    /**
     * Verifica se um objeto pode ser convertido para uma representação de data.
     *
     * @param object O objeto a ser verificado.
     * @return true se o objeto pode ser convertido para uma representação de data, false caso contrário.
     */
    public static boolean isDate(Object object) {
        if (object == null || object.toString().isEmpty()) {
            return false;
        }
        String strDate = object.toString();
        return DateConverter.stringBrToLocalDate(strDate) != null || DateConverter.stringTimeBrToLocalDateTime(strDate) != null || DateConverter.stringTimeToLocalDateTime(strDate) != null
                || DateConverter.stringToLocalDate(strDate, "yyyy-MM-dd") != null || DateConverter.stringToLocalDateTime(strDate, DATE_TIME_US_PATTERN) != null;
    }

    /**
     * Converte uma String no formato de data e hora com fuso horário para um objeto LocalDateTime.
     *
     * @param string A String a ser convertida para LocalDateTime, no formato "yyyy-MM-dd'T'HH:mm:ssZ".
     * @return Um objeto LocalDateTime representando a data e hora da String fornecida com fuso horário.
     * Retorna null em caso de exceção durante a conversão.
     */
    public static LocalDateTime stringTimeWithTimeZoneToLocalDateTime(String string) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_US_PATTERN);

            String[] aux = string.split("T");
            string = aux[0] + "T" + aux[1].substring(0, 5);
            return LocalDateTime.parse(string.replace("T", " "), formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Construtor privado para impedir a instanciação direta
     */
    private DateConverter() {
    }

}