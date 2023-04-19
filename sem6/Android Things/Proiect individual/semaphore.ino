#include <LiquidCrystal.h>

#define GREEN_LED_PIN 8
#define YELLOW_LED_PIN 9
#define RED_LED_PIN 10
#define BUTTON_PIN 6

const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);


enum SemaphoreMode {
  green,
  yellow,
  red
};
SemaphoreMode mode = green;

unsigned long myTime;
int buttonState = 0;
int lastButtonState = 0;
int crtPos = 0;

byte car[8] = {
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111
};

byte armsDown[8] = {
  0b00100,
  0b01010,
  0b00100,
  0b00100,
  0b01110,
  0b10101,
  0b00100,
  0b01010
};

void setup() {
  lcd.begin(16, 2);

  lcd.createChar(0, car);
  lcd.createChar(1, armsDown);

  pinMode(GREEN_LED_PIN, OUTPUT);
  pinMode(YELLOW_LED_PIN, OUTPUT);
  pinMode(RED_LED_PIN, OUTPUT);
  pinMode(BUTTON_PIN, INPUT);
}

void loop() {
  switch (mode) {
    case green:
      digitalWrite(GREEN_LED_PIN, HIGH);
      digitalWrite(YELLOW_LED_PIN, LOW);
      digitalWrite(RED_LED_PIN, LOW);

      lcd.setCursor(0, 1);
      lcd.write(byte(1));

      for (int i = 0; i < 16; i++) {
        if (i != 0) {
          lcd.setCursor(i - 1, 0);
          lcd.print(' ');
        }
        lcd.setCursor(i, 0);
        lcd.write(byte(0));

        if (i > 10) {
          if (i % 2 == 1) {
            digitalWrite(GREEN_LED_PIN, HIGH);
          } else {
            digitalWrite(GREEN_LED_PIN, LOW);
          }
        }
        delay(500);
      }
      lcd.clear();

      mode = yellow;
      break;

    case yellow:
      digitalWrite(GREEN_LED_PIN, LOW);
      digitalWrite(YELLOW_LED_PIN, HIGH);
      digitalWrite(RED_LED_PIN, LOW);

      lcd.setCursor(0, 0);
      lcd.write(byte(0));
      lcd.setCursor(0, 1);
      lcd.write(byte(1));
      delay(3000);
      lcd.clear();

      myTime = millis();
      crtPos = 0;
      mode = red;
      break;

    case red:
      digitalWrite(GREEN_LED_PIN, LOW);
      digitalWrite(YELLOW_LED_PIN, LOW);
      digitalWrite(RED_LED_PIN, HIGH);

      lcd.setCursor(0, 0);
      lcd.write(byte(0));

      if (millis() - myTime > 10000) {
        lcd.clear();
        mode = green;
      } else {
        lcd.setCursor(crtPos, 1);
        lcd.write(byte(1));

        buttonState = digitalRead(BUTTON_PIN);
        if (buttonState != lastButtonState) {
          if (buttonState == HIGH) {
            lcd.setCursor(crtPos, 1);
            lcd.print(' ');
            crtPos = (crtPos + 1) % 16;
            lcd.setCursor(crtPos, 1);
            lcd.write(byte(1));
          }
          delay(50);
        }
        lastButtonState = buttonState;
      }
      break;
  }
}