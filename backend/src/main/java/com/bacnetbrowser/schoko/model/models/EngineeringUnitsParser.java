package com.bacnetbrowser.schoko.model.models;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class EngineeringUnitsParser extends EngineeringUnits {

    public EngineeringUnitsParser(ByteQueue queue) throws BACnetErrorException {
        super(queue);
    }

        public static String toString(int type) {
            if (type == metersPerSecondPerSecond.intValue())
                return "m/s/s";
            if (type == squareMeters.intValue())
                return "m²";
            if (type == squareCentimeters.intValue())
                return "cm²";
            if (type == squareFeet.intValue())
                return "feet²";
            if (type == squareInches.intValue())
                return "inch²";
            if (type == currency1.intValue())
                return "currency 1";
            if (type == currency2.intValue())
                return "currency 2";
            if (type == currency3.intValue())
                return "currency 3";
            if (type == currency4.intValue())
                return "currency 4";
            if (type == currency5.intValue())
                return "currency 5";
            if (type == currency6.intValue())
                return "currency 6";
            if (type == currency7.intValue())
                return "currency 7";
            if (type == currency8.intValue())
                return "currency 8";
            if (type == currency9.intValue())
                return "currency 9";
            if (type == currency10.intValue())
                return "currency 10";
            if (type == milliamperes.intValue())
                return "mA";
            if (type == amperes.intValue())
                return "A";
            if (type == amperesPerMeter.intValue())
                return "A/m";
            if (type == amperesPerSquareMeter.intValue())
                return "A/m²";
            if (type == ampereSquareMeters.intValue())
                return "A²/m";
            if (type == farads.intValue())
                return "F";
            if (type == henrys.intValue())
                return "H";
            if (type == ohms.intValue())
                return "Ω";
            if (type == ohmMeters.intValue())
                return "Ωm";
            if (type == milliohms.intValue())
                return "mΩ";
            if (type == kilohms.intValue())
                return "kΩ";
            if (type == megohms.intValue())
                return "MΩ";
            if (type == siemens.intValue())
                return "S";
            if (type == siemensPerMeter.intValue())
                return "S/m";
            if (type == teslas.intValue())
                return "T";
            if (type == volts.intValue())
                return "V";
            if (type == millivolts.intValue())
                return "mV";
            if (type == kilovolts.intValue())
                return "kV";
            if (type == megavolts.intValue())
                return "MV";
            if (type == voltAmperes.intValue())
                return "VA";
            if (type == kilovoltAmperes.intValue())
                return "kVA";
            if (type == megavoltAmperes.intValue())
                return "MVA";
            if (type == voltAmperesReactive.intValue())
                return "Var";
            if (type == kilovoltAmperesReactive.intValue())
                return "kVar";
            if (type == megavoltAmperesReactive.intValue())
                return "MVAr";
            if (type == voltsPerDegreeKelvin.intValue())
                return "V/°K";
            if (type == voltsPerMeter.intValue())
                return "V/m";
            if (type == degreesPhase.intValue())
                return "φ";
            if (type == powerFactor.intValue())
                return "cos(φ)";
            if (type == webers.intValue())
                return "Wb";
            if (type == joules.intValue())
                return "J";
            if (type == kilojoules.intValue())
                return "kJ";
            if (type == kilojoulesPerKilogram.intValue())
                return "kJ/kg";
            if (type == megajoules.intValue())
                return "MJ";
            if (type == wattHours.intValue())
                return "Wh";
            if (type == kilowattHours.intValue())
                return "kWh";
            if (type == megawattHours.intValue())
                return "MWh";
            if (type == btus.intValue())
                return "BTU";
            if (type == kiloBtus.intValue())
                return "kBTU";
            if (type == megaBtus.intValue())
                return "MBTU";
            if (type == therms.intValue())
                return "thm";
            if (type == tonHours.intValue())
                return "ton hours";
            if (type == joulesPerKilogramDryAir.intValue())
                return "J/kgDA";
            if (type == kilojoulesPerKilogramDryAir.intValue())
                return "kJ/kgDA";
            if (type == megajoulesPerKilogramDryAir.intValue())
                return "MJ/kgDA";
            if (type == btusPerPoundDryAir.intValue())
                return "BTU/lbDA";
            if (type == btusPerPound.intValue())
                return "BTU/lb";
            if (type == joulesPerDegreeKelvin.intValue())
                return "J/°K";
            if (type == kilojoulesPerDegreeKelvin.intValue())
                return "kJ/°K";
            if (type == megajoulesPerDegreeKelvin.intValue())
                return "MJ/°K";
            if (type == joulesPerKilogramDegreeKelvin.intValue())
                return "J/kg°K";
            if (type == newton.intValue())
                return "N";
            if (type == cyclesPerHour.intValue())
                return "U/h";
            if (type == cyclesPerMinute.intValue())
                return "U/min";
            if (type == hertz.intValue())
                return "Hz";
            if (type == kilohertz.intValue())
                return "kHz";
            if (type == megahertz.intValue())
                return "MHz";
            if (type == perHour.intValue())
                return "/h";
            if (type == gramsOfWaterPerKilogramDryAir.intValue())
                return "gW/kgDA";
            if (type == percentRelativeHumidity.intValue())
                return "% g/m3";
            if (type == millimeters.intValue())
                return "mm";
            if (type == centimeters.intValue())
                return "cm";
            if (type == meters.intValue())
                return "m";
            if (type == inches.intValue())
                return "inch";
            if (type == feet.intValue())
                return "feet";
            if (type == candelas.intValue())
                return "lm";
            if (type == candelasPerSquareMeter.intValue())
                return "lm/m²";
            if (type == wattsPerSquareFoot.intValue())
                return "W/foot²";
            if (type == wattsPerSquareMeter.intValue())
                return "W/m²";
            if (type == lumens.intValue())
                return "lm";
            if (type == luxes.intValue())
                return "luxes";
            if (type == footCandles.intValue())
                return "foot lm";
            if (type == kilograms.intValue())
                return "kg";
            if (type == poundsMass.intValue())
                return "lb";
            if (type == tons.intValue())
                return "t";
            if (type == gramsPerSecond.intValue())
                return "g/s";
            if (type == gramsPerMinute.intValue())
                return "g/min";
            if (type == kilogramsPerSecond.intValue())
                return "kg/s";
            if (type == kilogramsPerMinute.intValue())
                return "kg/min";
            if (type == kilogramsPerHour.intValue())
                return "kg/h";
            if (type == poundsMassPerSecond.intValue())
                return "lb/s";
            if (type == poundsMassPerMinute.intValue())
                return "lb/min";
            if (type == poundsMassPerHour.intValue())
                return "lb/h";
            if (type == tonsPerHour.intValue())
                return "t/h";
            if (type == milliwatts.intValue())
                return "mW";
            if (type == watts.intValue())
                return "W";
            if (type == kilowatts.intValue())
                return "kW";
            if (type == megawatts.intValue())
                return "MW";
            if (type == btusPerHour.intValue())
                return "BTU/h";
            if (type == kiloBtusPerHour.intValue())
                return "kBTU/h";
            if (type == horsepower.intValue())
                return "PS";
            if (type == tonsRefrigeration.intValue())
                return "tons refrigeration";
            if (type == pascals.intValue())
                return "Pa";
            if (type == hectopascals.intValue())
                return "hPa";
            if (type == kilopascals.intValue())
                return "kPa";
            if (type == millibars.intValue())
                return "mbar";
            if (type == bars.intValue())
                return "bar";
            if (type == poundsForcePerSquareInch.intValue())
                return "lb/inch²";
            if (type == centimetersOfWater.intValue())
                return "cm Wa";
            if (type == inchesOfWater.intValue())
                return "inch Wa";
            if (type == millimetersOfMercury.intValue())
                return "mm Hg";
            if (type == centimetersOfMercury.intValue())
                return "cm Hg";
            if (type == inchesOfMercury.intValue())
                return "inch Hg";
            if (type == degreesCelsius.intValue())
                return "°C";
            if (type == degreesKelvin.intValue())
                return "°K";
            if (type == degreesKelvinPerHour.intValue())
                return "°K/h";
            if (type == degreesKelvinPerMinute.intValue())
                return "°K/min";
            if (type == degreesFahrenheit.intValue())
                return "°F";
            if (type == degreeDaysCelsius.intValue())
                return "°C day";
            if (type == degreeDaysFahrenheit.intValue())
                return "°F day";
            if (type == deltaDegreesFahrenheit.intValue())
                return "Δ°F";
            if (type == deltaDegreesKelvin.intValue())
                return "Δ°K";
            if (type == years.intValue())
                return "y";
            if (type == months.intValue())
                return "mon";
            if (type == weeks.intValue())
                return "week";
            if (type == days.intValue())
                return "day";
            if (type == hours.intValue())
                return "h";
            if (type == minutes.intValue())
                return "min";
            if (type == seconds.intValue())
                return "s";
            if (type == hundredthsSeconds.intValue())
                return "hs";
            if (type == milliseconds.intValue())
                return "ms";
            if (type == newtonMeters.intValue())
                return "NM";
            if (type == millimetersPerSecond.intValue())
                return "mm/s";
            if (type == millimetersPerMinute.intValue())
                return "mm/min";
            if (type == metersPerSecond.intValue())
                return "m/s";
            if (type == metersPerMinute.intValue())
                return "m/min";
            if (type == metersPerHour.intValue())
                return "m/h";
            if (type == kilometersPerHour.intValue())
                return "km/h";
            if (type == feetPerSecond.intValue())
                return "feet/s";
            if (type == feetPerMinute.intValue())
                return "feet/min";
            if (type == milesPerHour.intValue())
                return "mile/h";
            if (type == cubicFeet.intValue())
                return "feet3 ";
            if (type == cubicMeters.intValue())
                return "m3";
            if (type == imperialGallons.intValue())
                return "imperial gallons";
            if (type == liters.intValue())
                return "l";
            if (type == usGallons.intValue())
                return "us gallons";
            if (type == cubicFeetPerSecond.intValue())
                return "feet3/s";
            if (type == cubicFeetPerMinute.intValue())
                return "feet3/min";
            if (type == cubicMetersPerSecond.intValue())
                return " m3/s ";
            if (type == cubicMetersPerMinute.intValue())
                return "m3/min";
            if (type == cubicMetersPerHour.intValue())
                return "m3/h";
            if (type == imperialGallonsPerMinute.intValue())
                return "imperial gallons per minute";
            if (type == litersPerSecond.intValue())
                return "l/s";
            if (type == litersPerMinute.intValue())
                return "l/min";
            if (type == litersPerHour.intValue())
                return "l/h";
            if (type == usGallonsPerMinute.intValue())
                return "us gallons per minute";
            if (type == degreesAngular.intValue())
                return "degrees angular";
            if (type == degreesCelsiusPerHour.intValue())
                return "°C/h";
            if (type == degreesCelsiusPerMinute.intValue())
                return "°C/min";
            if (type == degreesFahrenheitPerHour.intValue())
                return "°F/h";
            if (type == degreesFahrenheitPerMinute.intValue())
                return "°F/min";
            if (type == jouleSeconds.intValue())
                return "J/s";
            if (type == kilogramsPerCubicMeter.intValue())
                return "kg/m3";
            if (type == kilowattHoursPerSquareMeter.intValue())
                return "kWh/m²";
            if (type == kilowattHoursPerSquareFoot.intValue())
                return "kWh/foot²";
            if (type == megajoulesPerSquareMeter.intValue())
                return "MJ/m²";
            if (type == megajoulesPerSquareFoot.intValue())
                return "MJ/foot²";
            if (type == noUnits.intValue())
                return "";
            if (type == newtonSeconds.intValue())
                return "Ns";
            if (type == newtonsPerMeter.intValue())
                return "N/m";
            if (type == partsPerMillion.intValue())
                return "ppm";
            if (type == partsPerBillion.intValue())
                return "ppb";
            if (type == percent.intValue())
                return "%";
            if (type == percentObscurationPerFoot.intValue())
                return "percent obscuration per foot";
            if (type == percentObscurationPerMeter.intValue())
                return "percent obscuration per meter";
            if (type == percentPerSecond.intValue())
                return "%/s";
            if (type == perMinute.intValue())
                return "/min";
            if (type == perSecond.intValue())
                return "/s";
            if (type == psiPerDegreeFahrenheit.intValue())
                return "psi per degree fahrenheit";
            if (type == radians.intValue())
                return "rad";
            if (type == radiansPerSecond.intValue())
                return "rad/s";
            if (type == revolutionsPerMinute.intValue())
                return "1/s";
            if (type == squareMetersPerNewton.intValue())
                return "m²/N";
            if (type == wattsPerMeterPerDegreeKelvin.intValue())
                return "W/m/°K";
            if (type == wattsPerSquareMeterDegreeKelvin.intValue())
                return "W/m²/°K";
            return "Unknown: " + type;
        }
    }

