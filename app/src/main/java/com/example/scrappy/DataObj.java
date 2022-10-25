/*
 *
 *   Created by Nishu Sharma on 26/10/22, 12:49 AM
 *   Copyright Ⓒ 2022. All rights reserved Ⓒ 2022 http://freefuninfo.com/
 *   Last modified: 26/10/22, 12:47 AM
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 *   except in compliance with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENS... Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *    either express or implied. See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.example.scrappy;

public class DataObj {
    long invoiceNo;
    String customerName;
    long date;

    public long getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(long invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getDate() {
        return date;
    }

    public DataObj() {
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getScrapType() {
        return scrapType;
    }

    public void setScrapType(String scrapType) {
        this.scrapType = scrapType;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    String scrapType;
    double Quantity;
    double amt;
}
