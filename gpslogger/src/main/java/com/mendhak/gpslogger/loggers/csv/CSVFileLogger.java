/*
 * Copyright (C) 2016 mendhak
 *
 * This file is part of GPSLogger for Android.
 *
 * GPSLogger for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * GPSLogger for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mendhak.gpslogger.loggers.csv;

import android.location.Location;
import android.support.annotation.Nullable;

import com.mendhak.gpslogger.common.BundleConstants;
import com.mendhak.gpslogger.common.Maths;
import com.mendhak.gpslogger.common.Session;
import com.mendhak.gpslogger.common.Strings;
import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.Files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;


public class CSVFileLogger implements FileLogger {

    private final Integer batteryLevel;
    private File file;
    protected final String name = "TXT";

    public CSVFileLogger(File file, @Nullable Integer batteryLevel) {
        this.file = file;
        this.batteryLevel = batteryLevel;
    }

    @Override
    public void write(Location loc) throws Exception {
        if (!Session.getInstance().hasDescription()) {
            annotate("", loc);
        }
    }

    String getCsvLine(Location loc, String dateTimeString) {
        return getCsvLine("", loc, dateTimeString);
    }

    String getCsvLine(String description, Location loc, String dateTimeString) {

        if (description.length() > 0) {
            description = "\"" + description.replaceAll("\"", "\"\"") + "\"";
        }

        String outputString = String.format(Locale.US, "%s %s\n",
                loc.getLatitude(),
                loc.getLongitude()
        );
        return outputString;
    }

    @Override
    public void annotate(String description, Location loc) throws Exception {
        if (!Files.reallyExists(file)) {
            file.createNewFile();

            FileOutputStream writer = new FileOutputStream(file, true);
            BufferedOutputStream output = new BufferedOutputStream(writer);
        //  String header = "time,lat,lon,elevation,accuracy,bearing,speed,satellites,provider,hdop,vdop,pdop,geoidheight,ageofdgpsdata,dgpsid,activity,battery,annotation\n";
            String header = ""; // no header in Flai file format
            output.write(header.getBytes());
            output.flush();
            output.close();

        }

        FileOutputStream writer = new FileOutputStream(file, true);
        BufferedOutputStream output = new BufferedOutputStream(writer);

        String dateTimeString = Strings.getIsoDateTime(new Date(loc.getTime()));
        String csvLine = getCsvLine(description, loc, dateTimeString);


        output.write(csvLine.getBytes());
        output.flush();
        output.close();
        Files.addToMediaDatabase(file, "text/csv");
    }

    @Override
    public String getName() {
        return name;
    }

}
