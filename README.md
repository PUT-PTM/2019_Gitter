# Gitter

## Table of Contents
* [Overview ](#Overview)
* [Description ](#Description)
* [Tools](#Tools)
* [How to use](#How-to-use)
* [Future improvements](#Future-improvements)
* [Attributions](#Attributions)
* [License](#License)
* [Credits](#Credits)

## Overview 
Our project is a gitter - yellow box which I had found on the beach.

## Description 
Equipment we used:
* STM32F407VG microcontroller 
* HD44780 2x16 alphanumeric LCD display

## Tools 
* Microcontroler is configured using the [STM32CubeMX](https://platformio.org) (5.2.1) and [System Workbench for STM32](https://www.st.com/en/development-tools/sw4stm32.html)
* Project is created using the [InteliJ IDEA](https://www.jetbrains.com/idea/)
* Language Java

## How to use
* Download Gitter.jar
* Plug-in device by USB
* Check at which port device appeard (Win + X > Device manager > Ports and there look for Port Com)
* Run Gitter.jar, chose correct port and enter repository URL\
(e.g. [https://github.com/PUT-PTM/2019_Gitter](https://github.com/PUT-PTM/2019_Gitter))
* Press button *Connect*

## Future improvements

* Construct a nicer casing
* Replace USB connection to wireless e.g. via Wi-Fi or Bluetooth 

## Attributions
* [Mohamed Yaqoob](https://www.youtube.com/watch?v=zfn5YqFIqbc) - creator of LCD1602 library

## License
MIT License 

## Credits 
* [Błażej Darul](https://github.com/darullef)
* [Jakub Florczak](https://github.com/flopczak)

The project was conducted during the Microprocessor Lab course held by the [Institute of Control and Information Engineering](http://www.cie.put.poznan.pl/index.php?lang=en), [Poznan University of Technology](https://www.put.poznan.pl/en).
Supervisor: [Tomasz Mańkowski](https://github.com/Tomasz-Mankowski)
