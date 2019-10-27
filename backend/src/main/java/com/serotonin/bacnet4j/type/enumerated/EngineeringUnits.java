/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import org.free.bacnet4j.util.ByteQueue;

public class EngineeringUnits extends Enumerated {
    private static final long serialVersionUID = -1334755490239859845L;
    // Acceleration
    public static final EngineeringUnits metersPerSecondPerSecond = new EngineeringUnits(166);
    // Area
    public static final EngineeringUnits squareMeters = new EngineeringUnits(0);
    public static final EngineeringUnits squareCentimeters = new EngineeringUnits(116);
    public static final EngineeringUnits squareFeet = new EngineeringUnits(1);
    public static final EngineeringUnits squareInches = new EngineeringUnits(115);
    // Currency
    public static final EngineeringUnits currency1 = new EngineeringUnits(105);
    public static final EngineeringUnits currency2 = new EngineeringUnits(106);
    public static final EngineeringUnits currency3 = new EngineeringUnits(107);
    public static final EngineeringUnits currency4 = new EngineeringUnits(108);
    public static final EngineeringUnits currency5 = new EngineeringUnits(109);
    public static final EngineeringUnits currency6 = new EngineeringUnits(110);
    public static final EngineeringUnits currency7 = new EngineeringUnits(111);
    public static final EngineeringUnits currency8 = new EngineeringUnits(112);
    public static final EngineeringUnits currency9 = new EngineeringUnits(113);
    public static final EngineeringUnits currency10 = new EngineeringUnits(114);
    // Electrical
    public static final EngineeringUnits milliamperes = new EngineeringUnits(2);
    public static final EngineeringUnits amperes = new EngineeringUnits(3);
    public static final EngineeringUnits amperesPerMeter = new EngineeringUnits(167);
    public static final EngineeringUnits amperesPerSquareMeter = new EngineeringUnits(168);
    public static final EngineeringUnits ampereSquareMeters = new EngineeringUnits(169);
    public static final EngineeringUnits farads = new EngineeringUnits(170);
    public static final EngineeringUnits henrys = new EngineeringUnits(171);
    public static final EngineeringUnits ohms = new EngineeringUnits(4);
    public static final EngineeringUnits ohmMeters = new EngineeringUnits(172);
    public static final EngineeringUnits milliohms = new EngineeringUnits(145);
    public static final EngineeringUnits kilohms = new EngineeringUnits(122);
    public static final EngineeringUnits megohms = new EngineeringUnits(123);
    public static final EngineeringUnits siemens = new EngineeringUnits(173); // 1 mho equals 1 siemens
    public static final EngineeringUnits siemensPerMeter = new EngineeringUnits(174);
    public static final EngineeringUnits teslas = new EngineeringUnits(175);
    public static final EngineeringUnits volts = new EngineeringUnits(5);
    public static final EngineeringUnits millivolts = new EngineeringUnits(124);
    public static final EngineeringUnits kilovolts = new EngineeringUnits(6);
    public static final EngineeringUnits megavolts = new EngineeringUnits(7);
    public static final EngineeringUnits voltAmperes = new EngineeringUnits(8);
    public static final EngineeringUnits kilovoltAmperes = new EngineeringUnits(9);
    public static final EngineeringUnits megavoltAmperes = new EngineeringUnits(10);
    public static final EngineeringUnits voltAmperesReactive = new EngineeringUnits(11);
    public static final EngineeringUnits kilovoltAmperesReactive = new EngineeringUnits(12);
    public static final EngineeringUnits megavoltAmperesReactive = new EngineeringUnits(13);
    public static final EngineeringUnits voltsPerDegreeKelvin = new EngineeringUnits(176);
    public static final EngineeringUnits voltsPerMeter = new EngineeringUnits(177);
    public static final EngineeringUnits degreesPhase = new EngineeringUnits(14);
    public static final EngineeringUnits powerFactor = new EngineeringUnits(15);
    public static final EngineeringUnits webers = new EngineeringUnits(178);
    // Energy
    public static final EngineeringUnits joules = new EngineeringUnits(16);
    public static final EngineeringUnits kilojoules = new EngineeringUnits(17);
    public static final EngineeringUnits kilojoulesPerKilogram = new EngineeringUnits(125);
    public static final EngineeringUnits megajoules = new EngineeringUnits(126);
    public static final EngineeringUnits wattHours = new EngineeringUnits(18);
    public static final EngineeringUnits kilowattHours = new EngineeringUnits(19);
    public static final EngineeringUnits megawattHours = new EngineeringUnits(146);
    public static final EngineeringUnits btus = new EngineeringUnits(20);
    public static final EngineeringUnits kiloBtus = new EngineeringUnits(147);
    public static final EngineeringUnits megaBtus = new EngineeringUnits(148);
    public static final EngineeringUnits therms = new EngineeringUnits(21);
    public static final EngineeringUnits tonHours = new EngineeringUnits(22);
    // Enthalpy
    public static final EngineeringUnits joulesPerKilogramDryAir = new EngineeringUnits(23);
    public static final EngineeringUnits kilojoulesPerKilogramDryAir = new EngineeringUnits(149);
    public static final EngineeringUnits megajoulesPerKilogramDryAir = new EngineeringUnits(150);
    public static final EngineeringUnits btusPerPoundDryAir = new EngineeringUnits(24);
    public static final EngineeringUnits btusPerPound = new EngineeringUnits(117);
    // Entropy
    public static final EngineeringUnits joulesPerDegreeKelvin = new EngineeringUnits(127);
    public static final EngineeringUnits kilojoulesPerDegreeKelvin = new EngineeringUnits(151);
    public static final EngineeringUnits megajoulesPerDegreeKelvin = new EngineeringUnits(152);
    public static final EngineeringUnits joulesPerKilogramDegreeKelvin = new EngineeringUnits(128);
    // Force
    public static final EngineeringUnits newton = new EngineeringUnits(153);
    // Frequency
    public static final EngineeringUnits cyclesPerHour = new EngineeringUnits(25);
    public static final EngineeringUnits cyclesPerMinute = new EngineeringUnits(26);
    public static final EngineeringUnits hertz = new EngineeringUnits(27);
    public static final EngineeringUnits kilohertz = new EngineeringUnits(129);
    public static final EngineeringUnits megahertz = new EngineeringUnits(130);
    public static final EngineeringUnits perHour = new EngineeringUnits(131);
    // Humidity
    public static final EngineeringUnits gramsOfWaterPerKilogramDryAir = new EngineeringUnits(28);
    public static final EngineeringUnits percentRelativeHumidity = new EngineeringUnits(29);
    // Length
    public static final EngineeringUnits millimeters = new EngineeringUnits(30);
    public static final EngineeringUnits centimeters = new EngineeringUnits(118);
    public static final EngineeringUnits meters = new EngineeringUnits(31);
    public static final EngineeringUnits inches = new EngineeringUnits(32);
    public static final EngineeringUnits feet = new EngineeringUnits(33);
    // Light
    public static final EngineeringUnits candelas = new EngineeringUnits(179);
    public static final EngineeringUnits candelasPerSquareMeter = new EngineeringUnits(180);
    public static final EngineeringUnits wattsPerSquareFoot = new EngineeringUnits(34);
    public static final EngineeringUnits wattsPerSquareMeter = new EngineeringUnits(35);
    public static final EngineeringUnits lumens = new EngineeringUnits(36);
    public static final EngineeringUnits luxes = new EngineeringUnits(37);
    public static final EngineeringUnits footCandles = new EngineeringUnits(38);
    // Mass
    public static final EngineeringUnits kilograms = new EngineeringUnits(39);
    public static final EngineeringUnits poundsMass = new EngineeringUnits(40);
    public static final EngineeringUnits tons = new EngineeringUnits(41);
    // Mass Flow
    public static final EngineeringUnits gramsPerSecond = new EngineeringUnits(154);
    public static final EngineeringUnits gramsPerMinute = new EngineeringUnits(155);
    public static final EngineeringUnits kilogramsPerSecond = new EngineeringUnits(42);
    public static final EngineeringUnits kilogramsPerMinute = new EngineeringUnits(43);
    public static final EngineeringUnits kilogramsPerHour = new EngineeringUnits(44);
    public static final EngineeringUnits poundsMassPerSecond = new EngineeringUnits(119);
    public static final EngineeringUnits poundsMassPerMinute = new EngineeringUnits(45);
    public static final EngineeringUnits poundsMassPerHour = new EngineeringUnits(46);
    public static final EngineeringUnits tonsPerHour = new EngineeringUnits(156);
    // Power
    public static final EngineeringUnits milliwatts = new EngineeringUnits(132);
    public static final EngineeringUnits watts = new EngineeringUnits(47);
    public static final EngineeringUnits kilowatts = new EngineeringUnits(48);
    public static final EngineeringUnits megawatts = new EngineeringUnits(49);
    public static final EngineeringUnits btusPerHour = new EngineeringUnits(50);
    public static final EngineeringUnits kiloBtusPerHour = new EngineeringUnits(157);
    public static final EngineeringUnits horsepower = new EngineeringUnits(51);
    public static final EngineeringUnits tonsRefrigeration = new EngineeringUnits(52);
    // Pressure
    public static final EngineeringUnits pascals = new EngineeringUnits(53);
    public static final EngineeringUnits hectopascals = new EngineeringUnits(133);
    public static final EngineeringUnits kilopascals = new EngineeringUnits(54);
    public static final EngineeringUnits millibars = new EngineeringUnits(134);
    public static final EngineeringUnits bars = new EngineeringUnits(55);
    public static final EngineeringUnits poundsForcePerSquareInch = new EngineeringUnits(56);
    public static final EngineeringUnits centimetersOfWater = new EngineeringUnits(57);
    public static final EngineeringUnits inchesOfWater = new EngineeringUnits(58);
    public static final EngineeringUnits millimetersOfMercury = new EngineeringUnits(59);
    public static final EngineeringUnits centimetersOfMercury = new EngineeringUnits(60);
    public static final EngineeringUnits inchesOfMercury = new EngineeringUnits(61);
    // Temperature
    public static final EngineeringUnits degreesCelsius = new EngineeringUnits(62);
    public static final EngineeringUnits degreesKelvin = new EngineeringUnits(63);
    public static final EngineeringUnits degreesKelvinPerHour = new EngineeringUnits(181);
    public static final EngineeringUnits degreesKelvinPerMinute = new EngineeringUnits(182);
    public static final EngineeringUnits degreesFahrenheit = new EngineeringUnits(64);
    public static final EngineeringUnits degreeDaysCelsius = new EngineeringUnits(65);
    public static final EngineeringUnits degreeDaysFahrenheit = new EngineeringUnits(66);
    public static final EngineeringUnits deltaDegreesFahrenheit = new EngineeringUnits(120);
    public static final EngineeringUnits deltaDegreesKelvin = new EngineeringUnits(121);
    // Time
    public static final EngineeringUnits years = new EngineeringUnits(67);
    public static final EngineeringUnits months = new EngineeringUnits(68);
    public static final EngineeringUnits weeks = new EngineeringUnits(69);
    public static final EngineeringUnits days = new EngineeringUnits(70);
    public static final EngineeringUnits hours = new EngineeringUnits(71);
    public static final EngineeringUnits minutes = new EngineeringUnits(72);
    public static final EngineeringUnits seconds = new EngineeringUnits(73);
    public static final EngineeringUnits hundredthsSeconds = new EngineeringUnits(158);
    public static final EngineeringUnits milliseconds = new EngineeringUnits(159);
    // Torque
    public static final EngineeringUnits newtonMeters = new EngineeringUnits(160);
    // Velocity
    public static final EngineeringUnits millimetersPerSecond = new EngineeringUnits(161);
    public static final EngineeringUnits millimetersPerMinute = new EngineeringUnits(162);
    public static final EngineeringUnits metersPerSecond = new EngineeringUnits(74);
    public static final EngineeringUnits metersPerMinute = new EngineeringUnits(163);
    public static final EngineeringUnits metersPerHour = new EngineeringUnits(164);
    public static final EngineeringUnits kilometersPerHour = new EngineeringUnits(75);
    public static final EngineeringUnits feetPerSecond = new EngineeringUnits(76);
    public static final EngineeringUnits feetPerMinute = new EngineeringUnits(77);
    public static final EngineeringUnits milesPerHour = new EngineeringUnits(78);
    // Volume
    public static final EngineeringUnits cubicFeet = new EngineeringUnits(79);
    public static final EngineeringUnits cubicMeters = new EngineeringUnits(80);
    public static final EngineeringUnits imperialGallons = new EngineeringUnits(81);
    public static final EngineeringUnits liters = new EngineeringUnits(82);
    public static final EngineeringUnits usGallons = new EngineeringUnits(83);
    // Volumetric Flow
    public static final EngineeringUnits cubicFeetPerSecond = new EngineeringUnits(142);
    public static final EngineeringUnits cubicFeetPerMinute = new EngineeringUnits(84);
    public static final EngineeringUnits cubicMetersPerSecond = new EngineeringUnits(85);
    public static final EngineeringUnits cubicMetersPerMinute = new EngineeringUnits(165);
    public static final EngineeringUnits cubicMetersPerHour = new EngineeringUnits(135);
    public static final EngineeringUnits imperialGallonsPerMinute = new EngineeringUnits(86);
    public static final EngineeringUnits litersPerSecond = new EngineeringUnits(87);
    public static final EngineeringUnits litersPerMinute = new EngineeringUnits(88);
    public static final EngineeringUnits litersPerHour = new EngineeringUnits(136);
    public static final EngineeringUnits usGallonsPerMinute = new EngineeringUnits(89);
    // Other
    public static final EngineeringUnits degreesAngular = new EngineeringUnits(90);
    public static final EngineeringUnits degreesCelsiusPerHour = new EngineeringUnits(91);
    public static final EngineeringUnits degreesCelsiusPerMinute = new EngineeringUnits(92);
    public static final EngineeringUnits degreesFahrenheitPerHour = new EngineeringUnits(93);
    public static final EngineeringUnits degreesFahrenheitPerMinute = new EngineeringUnits(94);
    public static final EngineeringUnits jouleSeconds = new EngineeringUnits(183);
    public static final EngineeringUnits kilogramsPerCubicMeter = new EngineeringUnits(186);
    public static final EngineeringUnits kilowattHoursPerSquareMeter = new EngineeringUnits(137);
    public static final EngineeringUnits kilowattHoursPerSquareFoot = new EngineeringUnits(138);
    public static final EngineeringUnits megajoulesPerSquareMeter = new EngineeringUnits(139);
    public static final EngineeringUnits megajoulesPerSquareFoot = new EngineeringUnits(140);
    public static final EngineeringUnits noUnits = new EngineeringUnits(95);
    public static final EngineeringUnits newtonSeconds = new EngineeringUnits(187);
    public static final EngineeringUnits newtonsPerMeter = new EngineeringUnits(188);
    public static final EngineeringUnits partsPerMillion = new EngineeringUnits(96);
    public static final EngineeringUnits partsPerBillion = new EngineeringUnits(97);
    public static final EngineeringUnits percent = new EngineeringUnits(98);
    public static final EngineeringUnits percentObscurationPerFoot = new EngineeringUnits(143);
    public static final EngineeringUnits percentObscurationPerMeter = new EngineeringUnits(144);
    public static final EngineeringUnits percentPerSecond = new EngineeringUnits(99);
    public static final EngineeringUnits perMinute = new EngineeringUnits(100);
    public static final EngineeringUnits perSecond = new EngineeringUnits(101);
    public static final EngineeringUnits psiPerDegreeFahrenheit = new EngineeringUnits(102);
    public static final EngineeringUnits radians = new EngineeringUnits(103);
    public static final EngineeringUnits radiansPerSecond = new EngineeringUnits(184);
    public static final EngineeringUnits revolutionsPerMinute = new EngineeringUnits(104);
    public static final EngineeringUnits squareMetersPerNewton = new EngineeringUnits(185);
    public static final EngineeringUnits wattsPerMeterPerDegreeKelvin = new EngineeringUnits(189);
    public static final EngineeringUnits wattsPerSquareMeterDegreeKelvin = new EngineeringUnits(141);

    public static final EngineeringUnits[] ALL = { squareMeters, squareFeet, milliamperes, amperes, ohms, volts,
            kilovolts, megavolts, voltAmperes, kilovoltAmperes, megavoltAmperes, voltAmperesReactive,
            kilovoltAmperesReactive, megavoltAmperesReactive, degreesPhase, powerFactor, joules, kilojoules, wattHours,
            kilowattHours, btus, therms, tonHours, joulesPerKilogramDryAir, btusPerPoundDryAir, cyclesPerHour,
            cyclesPerMinute, hertz, gramsOfWaterPerKilogramDryAir, percentRelativeHumidity, millimeters, meters,
            inches, feet, wattsPerSquareFoot, wattsPerSquareMeter, lumens, luxes, footCandles, kilograms, poundsMass,
            tons, kilogramsPerSecond, kilogramsPerMinute, kilogramsPerHour, poundsMassPerMinute, poundsMassPerHour,
            watts, kilowatts, megawatts, btusPerHour, horsepower, tonsRefrigeration, pascals, kilopascals, bars,
            poundsForcePerSquareInch, centimetersOfWater, inchesOfWater, millimetersOfMercury, centimetersOfMercury,
            inchesOfMercury, degreesCelsius, degreesKelvin, degreesFahrenheit, degreeDaysCelsius, degreeDaysFahrenheit,
            years, months, weeks, days, hours, minutes, seconds, metersPerSecond, kilometersPerHour, feetPerSecond,
            feetPerMinute, milesPerHour, cubicFeet, cubicMeters, imperialGallons, liters, usGallons,
            cubicFeetPerMinute, cubicMetersPerSecond, imperialGallonsPerMinute, litersPerSecond, litersPerMinute,
            usGallonsPerMinute, degreesAngular, degreesCelsiusPerHour, degreesCelsiusPerMinute,
            degreesFahrenheitPerHour, degreesFahrenheitPerMinute, noUnits, partsPerMillion, partsPerBillion, percent,
            percentPerSecond, perMinute, perSecond, psiPerDegreeFahrenheit, radians, revolutionsPerMinute, currency1,
            currency2, currency3, currency4, currency5, currency6, currency7, currency8, currency9, currency10,
            squareInches, squareCentimeters, btusPerPound, centimeters, poundsMassPerSecond, deltaDegreesFahrenheit,
            deltaDegreesKelvin, kilohms, megohms, millivolts, kilojoulesPerKilogram, megajoules, joulesPerDegreeKelvin,
            joulesPerKilogramDegreeKelvin, kilohertz, megahertz, perHour, milliwatts, hectopascals, millibars,
            cubicMetersPerHour, litersPerHour, kilowattHoursPerSquareMeter, kilowattHoursPerSquareFoot,
            megajoulesPerSquareMeter, megajoulesPerSquareFoot, wattsPerSquareMeterDegreeKelvin, cubicFeetPerSecond,
            percentObscurationPerFoot, percentObscurationPerMeter, milliohms, megawattHours, kiloBtus, megaBtus,
            kilojoulesPerKilogramDryAir, megajoulesPerKilogramDryAir, kilojoulesPerDegreeKelvin,
            megajoulesPerDegreeKelvin, newton, gramsPerSecond, gramsPerMinute, tonsPerHour, kiloBtusPerHour,
            hundredthsSeconds, milliseconds, newtonMeters, millimetersPerSecond, millimetersPerMinute, metersPerMinute,
            metersPerHour, cubicMetersPerMinute, metersPerSecondPerSecond, amperesPerMeter, amperesPerSquareMeter,
            ampereSquareMeters, farads, henrys, ohmMeters, siemens, siemensPerMeter, teslas, voltsPerDegreeKelvin,
            voltsPerMeter, webers, candelas, candelasPerSquareMeter, degreesKelvinPerHour, degreesKelvinPerMinute,
            jouleSeconds, radiansPerSecond, squareMetersPerNewton, kilogramsPerCubicMeter, newtonSeconds,
            newtonsPerMeter, wattsPerMeterPerDegreeKelvin, };

    public EngineeringUnits(int value) {
        super(value);
    }

    public EngineeringUnits(ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        int type = intValue();
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
