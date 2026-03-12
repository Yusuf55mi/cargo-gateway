<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ship="http://yurticikargo.com.tr/ShippingOrderDispatcherServices">
    <soapenv:Header/>
    <soapenv:Body>
        <ship:cancelShipment>
            <wsUserName>${username}</wsUserName>
            <wsPassword>${password}</wsPassword>
            <userLanguage>${userLanguage}</userLanguage>
            <cargoKeys>${trackingNo}</cargoKeys>
        </ship:cancelShipment>
    </soapenv:Body>
</soapenv:Envelope>