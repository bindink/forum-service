package telran.java51.post.dto.post;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class DatePeriodDto {
	LocalDate dateFrom;
    LocalDate dateTo;
}
