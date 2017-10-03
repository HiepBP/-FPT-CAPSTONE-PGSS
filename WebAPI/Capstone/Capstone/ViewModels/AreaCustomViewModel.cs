using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.ViewModels
{
    public class AreaUpdateViewModel
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int Status { get; set; }
    }

    public class AreaCustomViewModel : AreaViewModel
    {
        public int EmptyAmount { get; set; }
        public bool UpdateAvailable { get; set; }
    }
}