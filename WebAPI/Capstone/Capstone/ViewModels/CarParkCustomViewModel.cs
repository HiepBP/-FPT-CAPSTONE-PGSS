using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.ViewModels
{
    public class CarParkWithAmount
    {
        public CarParkViewModel CarPark { get; set; }
        public int? EmptyAmount { get; set; }
    }

    public class CarParkUpdateViewModel
    {
        public int Id { get; set; }
        public string Description { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
        public string Name { get; set; }
    }
}