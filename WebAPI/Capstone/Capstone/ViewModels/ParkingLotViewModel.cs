using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.ViewModels
{
    public partial class ParkingLotViewModel
    {
    }

    public class ParkingLotWithItemViewModel : ParkingLotViewModel
    {
        public IEnumerable<ItemViewModel> Items { get; set; }
    }
}