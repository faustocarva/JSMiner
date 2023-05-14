package br.unb.cic.js.date;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Interval {

    public enum Unit {
        Days(TimeUnit.DAYS);

        private TimeUnit unit;

        Unit(TimeUnit u) {
            unit = u;
        }
    }

    public static long diff(Date d, Date e, Unit u) {
        long diff = Math.abs(d.getTime() - e.getTime());

        return u.unit.convert(diff, TimeUnit.MILLISECONDS);
    }
}
