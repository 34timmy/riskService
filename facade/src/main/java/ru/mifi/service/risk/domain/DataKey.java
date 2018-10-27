package ru.mifi.service.risk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Ключ, уникально идентифицирующий строку данных
 *
 * @author DenRUS on 08.10.2017.
 */
@AllArgsConstructor
public class DataKey implements Comparable {

    private static final Logger LOG = LoggerFactory.getLogger(DataKey.class);
    /**
     * Год ключа данных
     */
    @Getter
    private final int year;
    /**
     * Инн ключа данных
     */
    @Getter
    private final String inn;

    /**
     * Системный метод сравннения объектов
     *
     * @param obj объект для сравнения
     * @return равен/не равен
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof DataKey &&
                this.inn.equalsIgnoreCase(((DataKey) obj).getInn()) &&
                this.year == ((DataKey) obj).getYear();
    }

    /**
     * Системный метод сравнения объектов
     *
     * @param o объект для сравнения
     * @return больше нуля - текущий объект больше анализируемого, 0 - равны, меньше нуля - наоборот
     */
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof DataKey)) {
            throw new ClassCastException("Попытка сравнить Ключ данных с другим объектом: " +
                    o.toString());
        }
        DataKey dk = (DataKey) o;
        return Objects.equals(dk.getInn(), this.getInn()) ?
                this.getYear() - dk.getYear() :
                this.getYear() - dk.getYear();
    }

    /**
     * Системный метод расчета хэшкода объекта
     *
     * @return хэшкод
     */
    @Override
    public int hashCode() {
        return 97 * year + 997 * inn.hashCode();
    }

    /**
     * Системный метод преобразования объекта в строку
     *
     * @return строка
     */
    @Override
    public String toString() {
        return getInn() + "/_/" + getYear();
    }
}
