import bluetooth
import threading
import time
import random
import sys

#address="00:17:E8:B2:50:28"
#uuid="00001101-0000-1000-8000-00805F9B34FB" 
#service_matches = bluetooth.find_service( uuid = uuid, address = address )

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

      while True:
        try:
          string = '11010110 \n'
          #1.karakter Sol sinyal
          #2.karakter Sag sinyal
          #3.karakter flash
          #4.karakter korna
          
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
