<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ship="http://yurticikargo.com.tr/ShippingOrderDispatcherServices">
    <soapenv:Header/>
    <soapenv:Body>
        <ship:queryShipment>
            <wsUserName>${username}</wsUserName>
            <wsPassword>${password}</wsPassword>
            <wsLanguage>${userLanguage}</wsLanguage>
            <keys>${trackingNo}</keys>
            <keyType>0</keyType>
            <addHistoricalData>true</addHistoricalData>
            <onlyTracking>false</onlyTracking>
        </ship:queryShipment>
    </soapenv:Body>
</soapenv:Envelope>