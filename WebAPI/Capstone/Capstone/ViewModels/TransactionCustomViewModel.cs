using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.ViewModels
{
    public class TransactionCustomViewModel : TransactionViewModel
    {
        public ParkingLotViewModel ParkingLot { get; set; }
    }
}