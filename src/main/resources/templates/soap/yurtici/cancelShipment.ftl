<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.integration.yurtici.com/">
    <soapenv:Header/>
    <soapenv:Body>
        <ws:cancelShipment>
            <request>
                <userInfo>
                    <userName>${username}</userName>
                    <password>${password}</password>
                    <userLanguage>${userLanguage}</userLanguage>
                </userInfo>
                <cargoKeys>
                    <cargoKey>${trackingNo}</cargoKey>
                </cargoKeys>
            </request>
        </ws:cancelShipment>
    </soapenv:Body>
</soapenv:Envelope>
