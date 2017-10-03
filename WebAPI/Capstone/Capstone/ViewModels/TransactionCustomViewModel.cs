using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.ViewModels
{
    public class TransactionCustomViewModel : TransactionViewModel
    {
        public ParkingLotViewModel ParkingLot { get; set; }
        public CarParkViewModel CarPark { get; set; }
    }

    public class TransactionCreateViewModel : TransactionViewModel
    {
        public string Username { get; set; }
        public int Duration { get; set; }
    }

    public class TransactionUpdateViewModel
    {
        public int Id { get; set; }
        public int Status { get; set; }
    }

    public class TransactionCreateReturnViewModel : TransactionViewModel
    {
        public long LongEndTime { get; set; }
    }
}