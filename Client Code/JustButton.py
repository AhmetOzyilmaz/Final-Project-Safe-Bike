import RPi.GPIO as GPIO
import time
GPIO.setmode(GPIO.BOARD)
GPIO.setup(11,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(13,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(15,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(18,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)

#GPIO.setup(7,GPIO.OUT)
#GPIO.output(7,0)

try:
        while True:
                #GPIO.output(7,GPIO.input(11))            
               
                if GPIO.input(13) == 1:
                        print "1"        
                if GPIO.input(11) == 1:
                        print "2"
                if GPIO.input(15) == 1:
                        print "3"
                if GPIO.input(18) == 1:
                        print "4"
                time.sleep(0.2)

except KeyboardInterrupt:
        GPIO.cleanup()

