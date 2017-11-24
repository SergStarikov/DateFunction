import java.time.LocalDate;
import java.time.Period;

public class Holiday {
    private LocalDate holidayStartDate;
    private LocalDate holidayEndDate;
    private Integer holidayDuration;

    public Holiday(LocalDate holidayStartDate, LocalDate holidayEndDate) {
        this.holidayStartDate = holidayStartDate;
        this.holidayEndDate = holidayEndDate;
        this.holidayDuration = Period.between(holidayStartDate, holidayEndDate).getDays() + 1;
    }

    public LocalDate getHolidayStartDate() {
        return holidayStartDate;
    }

    public LocalDate getHolidayEndDate() {
        return holidayEndDate;
    }

    public Integer getHolidayDuration() {
        return holidayDuration;
    }

}
