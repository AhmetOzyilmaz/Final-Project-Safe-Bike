import bluetooth
import threading
import time
import random
import sys
import RPi.GPIO as GPIO

#address="00:17:E8:B2:50:28"
#uuid="00001101-0000-1000-8000-00805F9B34FB" 
#service_matches = bluetooth.find_service( uuid = uuid, address = address )
GPIO.setmode(GPIO.BOARD)
GPIO.setup(11,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(13,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(15,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(18,GPIO.IN,pull_up_down = GPIO.PUD_DOWN)

uuid="00001101-0000-1000-8000-00805F9B34FB" 
while True:
  try:
    print "Searching ..."
    try: service_matches
    except NameError:
      service_matches = bluetooth.find_service( uuid = uuid)
    else:
      if not service_matches:
        print ("without address")
        service_matches = bluetooth.find_service( uuid = uuid)
      else:
        print ("with address")
        service_matches_with_addr = bluetooth.find_service( uuid = uuid, address = host )
        if service_matches_with_addr:
          service_matches = service_matches_with_addr
        else:
          continue

    if service_matches:
      first_match = service_matches[0]
      port = first_match["port"]
      name = first_match["name"]
      host = first_match["host"]
      print "connecting to \"%s\" on %s" % (name, host)
      sock=bluetooth.BluetoothSocket( bluetooth.RFCOMM )
      sock.connect((host, port))

      print "Happy Spamming"
      string = '0'

      while True:
        try:
          #1.karakter Sol sinyal
          #2.karakter Sag sinyal
          #3.karakter flash
          #4.karakter korna
          if GPIO.input(13) == 1:
                print "1"
                string = '1'

          elif GPIO.input(11) == 1:
                print '2'
                string = '3'

          elif GPIO.input(15) == 1:
               print '5'
               string = '5'

          elif GPIO.input(18) == 1:
            print '7'
            string = '7'





          time.sleep(0.2)
          try:
            sock.send(string)
            time.sleep(0.5)
          except:
            print "Android no longer interested in my spam, socket not valid, going back to searching"
            break
        except KeyboardInterrupt:
          print "Done with Spamming, Press Ctrl-C again if you wish to quit, otherwise I'll keep searching"
          break
  
  except KeyboardInterrupt:
    print "Phew! Done Searching"
    sys.exit()



sock.close()
