<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.integration.yurtici.com/">
    <soapenv:Header/>
    <soapenv:Body>
        <ws:createShipment>
            <request>
                <userInfo>
                    <userName>${username}</userName>
                    <password>${password}</password>
                    <userLanguage>${userLanguage}</userLanguage>
                </userInfo>
                <shipmentInfo>
                    <ShippingOrderVO>
                        <cargoCount>${cargoCount}</cargoCount>
                        <desi>${desi}</desi>
                        <receiverCityCode>0</receiverCityCode>
                        <receiverCityName>${receiver.city}</receiverCityName>
                        <receiverDistrictName>${receiver.district}</receiverDistrictName>
                        <receiverAddress>${receiver.address}</receiverAddress>
                        <receiverName>${receiver.firstName} ${receiver.lastName}</receiverName>
                        <receiverPhone>${receiver.phone}</receiverPhone>
                        <senderCityCode>0</senderCityCode>
                        <senderCityName>${sender.city}</senderCityName>
                        <senderDistrictName>${sender.district}</senderDistrictName>
                        <senderAddress>${sender.address}</senderAddress>
                        <senderName>${sender.firstName} ${sender.lastName}</senderName>
                        <senderPhone>${sender.phone}</senderPhone>
                        <referenceNo>${referenceNo}</referenceNo>
                        <waybillNo></waybillNo>
<#if hasCOD>
                        <ttCollectionPrice>${ttCollectionPrice}</ttCollectionPrice>
                        <ttCollectionType>${ttCollectionType}</ttCollectionType>
</#if>
                    </ShippingOrderVO>
                </shipmentInfo>
            </request>
        </ws:createShipment>
    </soapenv:Body>
</soapenv:Envelope>
